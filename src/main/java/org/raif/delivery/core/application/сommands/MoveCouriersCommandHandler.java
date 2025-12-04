package org.raif.delivery.core.application.сommands;

import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;

public interface MoveCouriersCommandHandler {
    UnitResult<Error> handle(MoveCouriersCommand command);
}
