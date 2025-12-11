package org.raif.delivery.core.application.commands;

import org.raif.libs.errs.Err;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.raif.libs.errs.UnitResult;

import java.util.UUID;

public record AddStoragePlaceCommand(UUID courierId, String name, int totalVolume) {
    public static Result<AddStoragePlaceCommand, Error> create(UUID courierId,
                                                               String name,
                                                               int totalVolume) {
        var validation = UnitResult.combine(
                Err.againstNullOrEmpty(courierId, "courierId"),
                Err.againstNullOrEmpty(name, "name"),
                Err.againstNegative(totalVolume, "totalVolume")
        );
        if (validation.isFailure()) return Result.failure(validation.getError());

        return Result.success(new AddStoragePlaceCommand(courierId, name, totalVolume));
    }
}
