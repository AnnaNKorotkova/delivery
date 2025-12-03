package org.raif.delivery.core.application.сommands;

import org.raif.libs.errs.UnitResult;

public interface CreateOrderCommandHandler {
    UnitResult<Error> handle(CreateOrderCommand command);
}
