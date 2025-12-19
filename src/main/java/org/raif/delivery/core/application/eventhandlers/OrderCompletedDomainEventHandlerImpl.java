package org.raif.delivery.core.application.eventhandlers;

import lombok.SneakyThrows;
import org.raif.delivery.core.domain.events.OrderCompletedDomainEvent;
import org.raif.delivery.core.ports.OrderEventProducer;
import org.raif.libs.errs.UnitResult;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCompletedDomainEventHandlerImpl implements OrderCompletedDomainEventHandler {

    private final OrderEventProducer<OrderCompletedDomainEvent> producer;

    public OrderCompletedDomainEventHandlerImpl(OrderEventProducer<OrderCompletedDomainEvent> producer) {
        this.producer = producer;
    }

    @EventListener
    @SneakyThrows
    public UnitResult<Error> handle(OrderCompletedDomainEvent event) {
        producer.publish(event);
        return UnitResult.success();
    }
}
