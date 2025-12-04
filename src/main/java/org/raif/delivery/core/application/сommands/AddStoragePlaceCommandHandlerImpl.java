package org.raif.delivery.core.application.сommands;

import jakarta.transaction.Transactional;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class AddStoragePlaceCommandHandlerImpl implements AddStoragePlaceCommandHandler {
    private final CourierRepository courierRepository;

    public AddStoragePlaceCommandHandlerImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public UnitResult<Error> handle(AddStoragePlaceCommand command) {
        var courier = courierRepository.findByCourierId(command.courierId());
        Courier updatedCourier = courier.orElse(null);
        if (updatedCourier == null) {
            return UnitResult.failure(Error.of("invalid.courier", "Courier cant be null"));
        }
        updatedCourier.addStoragePlace(command.name(), command.totalVolume());
        courierRepository.update(updatedCourier);
        return UnitResult.success();
    }
}
