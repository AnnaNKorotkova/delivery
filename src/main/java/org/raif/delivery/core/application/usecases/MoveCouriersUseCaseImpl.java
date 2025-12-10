package org.raif.delivery.core.application.usecases;

import org.raif.delivery.core.application.commands.MoveCouriersCommand;
import org.raif.delivery.core.application.commands.MoveCouriersCommandHandler;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveCouriersUseCaseImpl implements MoveCouriersUseCase {

    @Autowired
    private MoveCouriersCommandHandler moveCouriersCommandHandler;

    @Override
    public UnitResult<Error> handle() {
        UnitResult<Error> handle = moveCouriersCommandHandler.handle(new MoveCouriersCommand());
        if (handle.isFailure()) {
            throw new RuntimeException(handle.getError().getMessage());
        }
        return UnitResult.success();
    }
}
