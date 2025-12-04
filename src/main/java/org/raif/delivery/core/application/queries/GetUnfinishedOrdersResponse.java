package org.raif.delivery.core.application.queries;

import org.raif.delivery.core.application.queries.dto.OrderDto;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;

import java.util.List;

import static java.util.Collections.emptyList;

public record GetUnfinishedOrdersResponse(
        List<OrderDto> orders
) {
    public static Result<GetUnfinishedOrdersResponse, Error> create(List<OrderDto> orders) {
        if (orders == null || orders.isEmpty()) {
            return Result.success(new GetUnfinishedOrdersResponse(emptyList()));
        }

        return Result.success(new GetUnfinishedOrdersResponse(orders));
    }
}
