package org.raif.delivery.adapters.out.postgres;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByOrderId(UUID orderId);
}
