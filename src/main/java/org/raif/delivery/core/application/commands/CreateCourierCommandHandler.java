package org.raif.delivery.core.application.commands;

import org.raif.libs.errs.UnitResult;
import org.raif.libs.errs.Error;

public interface CreateCourierCommandHandler {
    UnitResult<Error> handle(CreateCourierCommand command);
}
