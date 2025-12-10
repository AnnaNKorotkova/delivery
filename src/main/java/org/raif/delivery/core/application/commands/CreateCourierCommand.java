package org.raif.delivery.core.application.commands;

import org.raif.libs.errs.Err;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.raif.libs.errs.UnitResult;

public record CreateCourierCommand(String name, int speed) {
    public static Result<CreateCourierCommand, Error> create(String name, int speed) {
        var validation = UnitResult.combine(
                Err.againstNullOrEmpty(name, "name"),
                Err.againstNegative(speed, "speed")
        );
        if (validation.isFailure()) return Result.failure(validation.getError());

        return Result.success(new CreateCourierCommand(name, speed));
    }
}
