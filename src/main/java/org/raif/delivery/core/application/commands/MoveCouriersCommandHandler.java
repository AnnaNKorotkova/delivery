package org.raif.delivery.core.application.commands;

import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;

public interface MoveCouriersCommandHandler {
    UnitResult<Error> handle(MoveCouriersCommand command);
}
