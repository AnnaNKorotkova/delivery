package org.raif.delivery.core.application.queries;

import java.util.List;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.raif.delivery.core.domain.model.courier.Courier;

public interface GetAllCouriersQueryHandler {
    Result<GetCouriersResponse, Error> handle();
}
