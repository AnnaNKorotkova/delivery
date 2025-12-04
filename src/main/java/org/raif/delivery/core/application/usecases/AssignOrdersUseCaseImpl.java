package org.raif.delivery.core.application.usecases;

import org.raif.delivery.core.application.сommands.AssignOrderCommand;
import org.raif.delivery.core.application.сommands.AssignOrderCommandHandler;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignOrdersUseCaseImpl implements AssignOrdersUseCase {

    @Autowired
    private AssignOrderCommandHandler assignOrderCommandHandler;

    @Override
    public UnitResult<Error> handle() {
        UnitResult<Error> handle = assignOrderCommandHandler.handle(new AssignOrderCommand());
        if (handle.isFailure()) {
            return UnitResult.failure(Error.of(handle.getError().getCode(), handle.getError().getMessage()));
        }
        return UnitResult.success();
    }
}
