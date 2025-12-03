package org.raif.delivery.core.domain.services;

import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;

import java.util.List;

public interface OrderDispatcher {

    Result<Courier, Error> dispatchOrder(Order order, List<Courier> couriers);
}
