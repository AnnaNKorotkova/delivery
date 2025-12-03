package org.raif.delivery.core.application.queries;

import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;

public interface GetUnfinishedOrdersQueryHandler {
    Result<GetUnfinishedOrdersResponse, Error> handle();
}
