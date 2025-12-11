package org.raif.delivery.core.application.commands;

import jakarta.transaction.Transactional;
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

    public CreateOrderCommandHandlerImpl(OrderRepository orderRepository, GeoClient geoClient) {
        this.orderRepository = orderRepository;
        this.geoClient = geoClient;
    }

    @Override
    @Transactional
    public UnitResult<Error> handle(CreateOrderCommand command) {
        Result<Location, Error> location = geoClient.getLocation(command.street());
        if (location.isFailure()) {
            return UnitResult.failure(location.getError());
        }
        orderRepository.save(Order.create(command.orderId(), location.getValue(), command.volume()).getValue());
        return UnitResult.success();
    }
}
