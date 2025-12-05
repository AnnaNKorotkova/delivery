package org.raif.delivery.core.application.сommands;

import org.raif.libs.errs.UnitResult;
import org.raif.libs.errs.Error;

public interface CreateOrderCommandHandler {
    UnitResult<Error> handle(CreateOrderCommand command);
}
