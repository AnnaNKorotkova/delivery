package org.raif.delivery.adapters.out.postgres;

import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.delivery.core.ports.UnitOfWork;
import org.raif.delivery.libs.errs.Except;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CourierRepositoryImpl implements CourierRepository {

    private final UnitOfWork unitOfWork;
    private final CourierJpaRepository repository;

    public CourierRepositoryImpl(UnitOfWork unitOfWork, CourierJpaRepository repository) {
        this.unitOfWork = unitOfWork;
        this.repository = repository;
    }

    @Override
    public void save(Courier courier) {
        Except.againstNull(courier, "courier");
        unitOfWork.getAggregateTracker().track(courier);
    }

    @Override
    public void update(Courier courier) {
        Except.againstNull(courier, "courier");
        unitOfWork.getAggregateTracker().track(courier);
    }

    @Override
    public Optional<Courier> findById(UUID courierId) {
        var courier = repository.findById(courierId);
        courier.ifPresent(unitOfWork.getAggregateTracker()::track);
        return courier;
    }

    @Override
    public List<Courier> findFreeCouriers() {
        var couriers = repository.findAll();
        var listFreeCouriers = couriers.stream()
                .filter(
                        courier -> courier.getStoragePlaces().stream()
                                .anyMatch(palace -> palace.getOrderId() == null)).toList();
        listFreeCouriers.forEach(unitOfWork.getAggregateTracker()::track);
        return listFreeCouriers;
    }
}
