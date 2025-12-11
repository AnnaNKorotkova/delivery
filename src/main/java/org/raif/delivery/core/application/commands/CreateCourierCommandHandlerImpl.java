package org.raif.delivery.core.application.commands;

import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;
import org.springframework.stereotype.Service;

@Service
public class CreateCourierCommandHandlerImpl implements CreateCourierCommandHandler {
    private final CourierRepository courierRepository;

    public CreateCourierCommandHandlerImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public UnitResult<Error> handle(CreateCourierCommand command) {
        courierRepository.save(Courier.create(command.name(), command.speed(), Location.create(1, 1).getValue()).getValue());
        return UnitResult.success();
    }
}
