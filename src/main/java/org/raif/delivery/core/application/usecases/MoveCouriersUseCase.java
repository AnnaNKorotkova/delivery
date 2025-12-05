package org.raif.delivery.core.application.usecases;

import org.raif.libs.errs.Error;
import org.raif.libs.errs.UnitResult;

public interface MoveCouriersUseCase {
    UnitResult<Error> handle();
}
