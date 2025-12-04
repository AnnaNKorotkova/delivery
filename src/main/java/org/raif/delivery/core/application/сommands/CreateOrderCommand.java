package org.raif.delivery.core.application.сommands;

import org.raif.libs.errs.Err;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.raif.libs.errs.UnitResult;

import java.util.UUID;

public record CreateOrderCommand(UUID orderId, String street, int volume) {
    public static Result<CreateOrderCommand, Error> create(UUID orderId,
                                                           String street,
                                                           int volume) {
        var validation = UnitResult.combine(
                Err.againstNullOrEmpty(orderId, "orderId"),
                Err.againstNullOrEmpty(street, "street"),
                Err.againstNegative(volume, "volume")
        );
        if (validation.isFailure()) return Result.failure(validation.getError());

        return Result.success(new CreateOrderCommand(orderId, street, volume));
    }
}
