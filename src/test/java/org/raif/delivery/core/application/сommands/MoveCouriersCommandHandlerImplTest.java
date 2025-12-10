package org.raif.delivery.core.application.сommands;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.raif.delivery.core.application.commands.MoveCouriersCommand;
import org.raif.delivery.core.application.commands.MoveCouriersCommandHandlerImpl;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MoveCouriersCommandHandlerImplTest {

    private final CourierRepository courierRepository = mock(CourierRepository.class);

    @Test
    void shouldMoveCourier() {
        var courier = Courier.create("Иван", 1, Location.create(1, 1).getValue()).getValue();
        when(courierRepository.findFreeCouriers()).thenReturn(List.of(courier));

        var handler = new MoveCouriersCommandHandlerImpl(courierRepository);
        var command = MoveCouriersCommand.create().getValue();

        UnitResult<Error> handle = handler.handle(command);

        assertTrue(handle.isSuccess());
    }
}