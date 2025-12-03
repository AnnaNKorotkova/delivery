package org.raif.delivery.core.application.queries;

import org.raif.delivery.core.application.queries.dto.CourierDto;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;

import java.util.Collections;
import java.util.List;

public record GetCouriersResponse(
        List<CourierDto> couriers
) {
    public static Result<GetCouriersResponse, Error> create(List<CourierDto> couriers) {
        if (couriers == null || couriers.isEmpty()) {
            return  Result.success(new GetCouriersResponse(Collections.emptyList()));
        }
        return Result.success(new GetCouriersResponse(couriers));
    }
}
