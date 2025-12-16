package org.raif.delivery.core.application.eventhandlers;

import lombok.SneakyThrows;
import org.raif.delivery.core.domain.events.OrderCreatedDomainEvent;
import org.raif.delivery.core.ports.OrderEventProducer;
import org.raif.libs.errs.UnitResult;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCreatedDomainEventHandlerImpl implements OrderCreatedDomainEventHandler {

    private final OrderEventProducer<OrderCreatedDomainEvent> producer;

    public OrderCreatedDomainEventHandlerImpl(OrderEventProducer<OrderCreatedDomainEvent> producer) {
        this.producer = producer;
    }

    @EventListener
    @SneakyThrows
    public UnitResult<Error> handle(OrderCreatedDomainEvent event){
        producer.publish(event);
        return UnitResult.success();
    }
}
