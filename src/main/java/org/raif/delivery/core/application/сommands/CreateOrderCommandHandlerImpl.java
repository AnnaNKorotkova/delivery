package org.raif.delivery.core.application.сommands;

import jakarta.transaction.Transactional;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.ports.OrderRepository;
import org.raif.libs.errs.UnitResult;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderCommandHandlerImpl implements CreateOrderCommandHandler {
    private final OrderRepository orderRepository;

    public CreateOrderCommandHandlerImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public UnitResult<Error> handle(CreateOrderCommand command) {
        orderRepository.save(Order.create(command.orderId(), Location.create(1, 1).getValue(), command.volume()).getValue());

        return UnitResult.success();
    }
}
