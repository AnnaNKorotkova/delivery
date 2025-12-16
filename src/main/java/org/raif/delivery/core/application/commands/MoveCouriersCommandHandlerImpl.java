package org.raif.delivery.core.application.commands;

import jakarta.transaction.Transactional;
import java.util.List;
import org.raif.delivery.adapters.out.kafka.OrderCompletedEventProducer;
import org.raif.delivery.core.domain.events.OrderCompletedDomainEvent;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.delivery.core.ports.OrderRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;
import org.springframework.stereotype.Service;

@Service
public class MoveCouriersCommandHandlerImpl implements MoveCouriersCommandHandler {

    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;
    private final OrderCompletedEventProducer producer;

    public MoveCouriersCommandHandlerImpl(CourierRepository courierRepository, OrderRepository orderRepository, OrderCompletedEventProducer producer) {
        this.courierRepository = courierRepository;
        this.orderRepository = orderRepository;
        this.producer = producer;
    }

    @Override
    @Transactional
    public UnitResult<Error> handle(MoveCouriersCommand command) {
        List<Order> orders = orderRepository.findByAssignedStatus();
        List<Courier> couriers = courierRepository.getAllCouriers();
        orders.forEach(order -> {
            couriers.forEach(courier -> {
                if (courier.getLocation().equals(order.getLocation())) {
                    order.terminateOrder();
                    orderRepository.update(order);
                    courier.terminateOrder(order);
                    courierRepository.update(courier);
                    producer.publish(new OrderCompletedDomainEvent(order));
                    return;
                }

                if (order.getCourierId().equals(courier.getCourierId())) {
                    courier.move(order.getLocation());
                    courierRepository.update(courier);
                }
            });
        });

        return UnitResult.success();
    }
}
