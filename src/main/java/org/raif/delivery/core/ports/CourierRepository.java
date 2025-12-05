package org.raif.delivery.core.ports;

import org.raif.delivery.core.domain.model.courier.Courier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourierRepository {
    void save(Courier courier);

    void update(Courier courier);

    Optional<Courier> findByCourierId(UUID courierId);

    List<Courier> findFreeCouriers();

    List<Courier> getAllCouriers();
}
