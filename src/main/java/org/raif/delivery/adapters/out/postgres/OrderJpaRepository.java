package org.raif.delivery.adapters.out.postgres;

import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    List<Order> findByStatus(OrderStatus status);
}
