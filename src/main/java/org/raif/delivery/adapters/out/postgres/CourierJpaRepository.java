package org.raif.delivery.adapters.out.postgres;

import java.util.Optional;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourierJpaRepository extends JpaRepository<Courier, UUID> {
    List<Courier> findAll();
    Optional<Courier> findByCourierId(UUID courierId);
}
