package org.raif.delivery.adapters.out.postgres;

import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.raif.delivery.core.ports.OrderRepository;
import org.raif.libs.errs.Except;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository repository;

    public OrderRepositoryImpl(OrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Order order) {
        Except.againstNull(order, "order");
        repository.save(order);
    }

    @Override
    public void update(Order order) {
        Except.againstNull(order, "order");
        repository.save(order);
    }

    @Override
    public Optional<Order> findByOrderId(UUID orderId) {
        return repository.findByOrderId(orderId);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<Order> findByAssignedStatus() {
        return repository.findByStatus(OrderStatus.ASSIGNED);
    }
}
