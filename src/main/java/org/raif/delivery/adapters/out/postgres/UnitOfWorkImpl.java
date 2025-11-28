package org.raif.delivery.adapters.out.postgres;

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.ports.AggregateTracker;
import org.raif.delivery.core.ports.UnitOfWork;
import org.raif.delivery.libs.ddd.AggregateRoot;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UnitOfWorkImpl implements UnitOfWork {
    @Getter
    private final AggregateTracker aggregateTracker;
    private final OrderJpaRepository orderJpaRepository;
    private final CourierJpaRepository courierJpaRepository;

    public UnitOfWorkImpl(AggregateTracker aggregateTracker,
                          OrderJpaRepository orderJpaRepository,
                          CourierJpaRepository courierJpaRepository) {
        this.aggregateTracker = aggregateTracker;
        this.orderJpaRepository = orderJpaRepository;
        this.courierJpaRepository = courierJpaRepository;
    }

    @Transactional
    @Override
    public void commit() {
        for (AggregateRoot aggregate : aggregateTracker.getTracked()) {
            if (aggregate instanceof Order) orderJpaRepository.save((Order) aggregate);
            if (aggregate instanceof Courier) courierJpaRepository.save((Courier) aggregate);
        }
        aggregateTracker.clear();
    }
}