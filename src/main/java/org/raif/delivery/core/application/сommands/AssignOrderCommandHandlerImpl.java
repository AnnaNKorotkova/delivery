package org.raif.delivery.core.application.сommands;

import jakarta.transaction.Transactional;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.delivery.core.ports.OrderRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AssignOrderCommandHandlerImpl implements AssignOrderCommandHandler {

    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;

    public AssignOrderCommandHandlerImpl(CourierRepository courierRepository, OrderRepository orderRepository) {
        this.courierRepository = courierRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public UnitResult<Error> handle(AssignOrderCommand command) {
        Courier courier = courierRepository.findFreeCouriers().stream()
                .findFirst()
                .orElse(null);
        Order order = orderRepository.findByStatus(OrderStatus.CREATED).stream()
                .findFirst()
                .orElse(null);
        if (courier == null) {
            return UnitResult.failure(Error.of("courier.empty", "not found free courier"));
        }

        if (order == null) {
            return UnitResult.failure(Error.of("courier.empty", "not found free courier"));
        }
        courier.takeOrder(order);
        courierRepository.save(courier);
        return UnitResult.success();
    }
}
