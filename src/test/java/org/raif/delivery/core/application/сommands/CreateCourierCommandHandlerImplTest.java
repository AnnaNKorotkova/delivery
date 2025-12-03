package org.raif.delivery.core.application.сommands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreateCourierCommandHandlerImplTest {

    private final CourierRepository courierRepository = mock(CourierRepository.class);

    @Test
    public void shouldCreateCourier() {
        var handler = new CreateCourierCommandHandlerImpl(courierRepository);
        var command = CreateCourierCommand.create("Иван", 3).getValue();

        UnitResult<Error> handle = handler.handle(command);

        Assertions.assertTrue(handle.isSuccess());
        verify(courierRepository).save(any(Courier.class));

    }

}