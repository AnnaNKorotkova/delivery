package org.raif.delivery.core.domain.services;

import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderDispatcherImpl implements OrderDispatcher {
    @Override
    public Result<Courier, Error> dispatchOrder(Order order, List<Courier> couriers) {
        if (couriers.isEmpty()) {
            return Result.failure(Error.of("no.courier", "cant find courier"));
        }

        Courier fastestCourier = couriers.stream()
                .min(Comparator.comparingDouble(courier -> courier.countStepSpeedCourier(order.getLocation()).getValue()))
                .get();

        if (fastestCourier.takeOrder(order).isFailure()) {
            return Result.failure(Error.of("no.courier", "courier can take order"));
        }
        fastestCourier.takeOrder(order);
        return Result.success(fastestCourier);
    }
}
