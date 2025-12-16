package org.raif.delivery.core.application.commands;

import java.util.List;
import org.raif.delivery.DomainEventPublisher;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;
import org.springframework.stereotype.Service;

@Service
public class CreateCourierCommandHandlerImpl implements CreateCourierCommandHandler {
    private final CourierRepository courierRepository;
    private final DomainEventPublisher domainEventPublisher;

    public CreateCourierCommandHandlerImpl(CourierRepository courierRepository, DomainEventPublisher domainEventPublisher) {
        this.courierRepository = courierRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public UnitResult<Error> handle(CreateCourierCommand command) {
        var courier = Courier.create(command.name(), command.speed(), Location.create(1, 1).getValue()).getValue();
        courierRepository.save(courier);
        domainEventPublisher.publish(List.of(courier));
        return UnitResult.success();
    }
}
