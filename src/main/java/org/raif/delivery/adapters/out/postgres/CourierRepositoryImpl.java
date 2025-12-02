package org.raif.delivery.adapters.out.postgres;

import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.delivery.libs.errs.Except;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CourierRepositoryImpl implements CourierRepository {

    private final CourierJpaRepository repository;

    public CourierRepositoryImpl(CourierJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Courier courier) {
        Except.againstNull(courier, "courier");
        repository.save(courier);
    }

    @Override
    public void update(Courier courier) {
        Except.againstNull(courier, "courier");
        repository.save(courier);
    }

    @Override
    public Optional<Courier> findById(UUID courierId) {
        var courier = repository.findById(courierId);
        return courier;
    }

    @Override
    public List<Courier> findFreeCouriers() {
        var couriers = repository.findAll();
        return couriers.stream()
                .filter(
                        courier -> courier.getStoragePlaces().stream()
                                .anyMatch(palace -> palace.getOrderId() == null)).toList();
    }
}
