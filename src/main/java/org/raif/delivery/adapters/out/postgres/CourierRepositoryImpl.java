package org.raif.delivery.adapters.out.postgres;

import jakarta.transaction.Transactional;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Except;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
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
    public Optional<Courier> findByCourierId(UUID courierId) {
        var courier = repository.findByCourierId(courierId);
        return courier;
    }

    @Override
    public List<Courier> findFreeCouriers() {
        var couriers = repository.findAll();
        var v =  couriers.stream()
                .filter(
                        courier -> courier.getStoragePlaces().stream()
                                .anyMatch(palace -> palace.getOrderId() == null))
                .toList();
        return v;
    }

    @Override
    public List<Courier> getAllCouriers() {
        return repository.findAll();
    }
}
