package org.raif.delivery.core.application.сommands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.application.commands.AddStoragePlaceCommand;
import org.raif.delivery.core.application.commands.AddStoragePlaceCommandHandlerImpl;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class AddStoragePlaceCommandHandlerImplTest extends BaseTest {

    private final CourierRepository courierRepository = mock(CourierRepository.class);

    @Test
    void shouldAddStoragePlace() {
        var courierId = UUID.randomUUID();
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        var handler = new AddStoragePlaceCommandHandlerImpl(courierRepository);
        var command = AddStoragePlaceCommand.create(courierId, "сумка", 15).getValue();
        when(courierRepository.findByCourierId(courierId)).thenReturn(Optional.of(courier));

        UnitResult<Error> handle = handler.handle(command);

        Assertions.assertTrue(handle.isSuccess());
        verify(courierRepository).update(courier);
    }


    @Test
    void shouldNotFoundCourier() {
        var courierId = UUID.randomUUID();
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        var handler = new AddStoragePlaceCommandHandlerImpl(courierRepository);
        var command = AddStoragePlaceCommand.create(courierId, "сумка", 15).getValue();
        when(courierRepository.findByCourierId(courierId)).thenReturn(Optional.empty());

        UnitResult<Error> handle = handler.handle(command);

        Assertions.assertTrue(handle.isFailure());
        verify(courierRepository, never()).update(any(Courier.class));
    }
}