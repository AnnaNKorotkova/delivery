package org.raif.delivery.adapters.out.postgres;

import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.raif.delivery.core.ports.OrderRepository;
import org.raif.delivery.core.ports.UnitOfWork;
import org.raif.delivery.libs.errs.Except;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final UnitOfWork unitOfWork;
    private final OrderJpaRepository repository;

    public OrderRepositoryImpl(UnitOfWork unitOfWork, OrderJpaRepository repository) {
        this.unitOfWork = unitOfWork;
        this.repository = repository;
    }

    @Override
    public void save(Order order) {
        Except.againstNull(order, "order");
        unitOfWork.getAggregateTracker().track(order);
    }

    @Override
    public void update(Order order) {
        Except.againstNull(order, "order");
        unitOfWork.getAggregateTracker().track(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        var order = repository.findById(orderId);
        order.ifPresent(unitOfWork.getAggregateTracker()::track);
        return order;
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        var orders = repository.findByStatus(status);
        orders.forEach(unitOfWork.getAggregateTracker()::track);
        return orders;
    }

    @Override
    public List<Order> findByAssignedStatus() {
        var orders = repository.findByStatus(OrderStatus.ASSIGNED);
        orders.forEach(unitOfWork.getAggregateTracker()::track);
        return orders;
    }
}
