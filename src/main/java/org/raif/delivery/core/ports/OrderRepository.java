package org.raif.delivery.core.ports;

import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void save(Order order);

    void update(Order order);

    Optional<Order> findByOrderId(UUID orderId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByAssignedStatus();
}
