package org.raif.delivery.core.application.commands;

import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;

public record MoveCouriersCommand() {
    public static Result<MoveCouriersCommand, Error> create() {
        return Result.success(new MoveCouriersCommand());
    }
}
