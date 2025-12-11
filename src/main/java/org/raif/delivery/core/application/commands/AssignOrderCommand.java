package org.raif.delivery.core.application.commands;

import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;


public record AssignOrderCommand() {
    public static Result<AssignOrderCommand, Error> create() {
        return Result.success(new AssignOrderCommand());
    }
}
