package org.raif.delivery.core.application.commands;

import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;
import org.springframework.stereotype.Service;

@Service
public class MoveCouriersCommandHandlerImpl implements MoveCouriersCommandHandler {

    private final CourierRepository courierRepository;

    public MoveCouriersCommandHandlerImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public UnitResult<Error> handle(MoveCouriersCommand command) {
        courierRepository.findFreeCouriers().forEach(courier -> courier.move(Location.create(5, 5).getValue()));
        return UnitResult.success();
    }
}
