package org.raif.delivery.core.application.commands;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.SneakyThrows;
import org.raif.delivery.DomainEventPublisher;
import org.raif.delivery.adapters.out.kafka.OrderCreatedEventProducer;
import org.raif.delivery.core.domain.events.OrderCreatedDomainEvent;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.ports.GeoClient;
import org.raif.delivery.core.ports.OrderRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.raif.libs.errs.UnitResult;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderCommandHandlerImpl implements CreateOrderCommandHandler {
    private final OrderRepository orderRepository;
    private final GeoClient geoClient;
    private final DomainEventPublisher domainEventPublisher;

    public CreateOrderCommandHandlerImpl(OrderRepository orderRepository, GeoClient geoClient, DomainEventPublisher domainEventPublisher, OrderCreatedEventProducer producer, DomainEventPublisher domainEventPublisher1) {
        this.orderRepository = orderRepository;
        this.geoClient = geoClient;
        this.domainEventPublisher = domainEventPublisher1;
    }

    @Override
    @Transactional
    @SneakyThrows
    public UnitResult<Error> handle(CreateOrderCommand command) {
        Result<Location, Error> location = geoClient.getLocation(command.street());
        if (location.isFailure()) {
            return UnitResult.failure(location.getError());
        }
        var order = Order.create(command.orderId(), location.getValue(), command.volume()).getValue();
        orderRepository.save(order);
        domainEventPublisher.publish(List.of(order));
        return UnitResult.success();
    }
}
