package org.raif.delivery.core.application.queries;

import org.raif.delivery.core.application.queries.dto.CourierDto;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllCouriersQueryHandlerImpl implements GetAllCouriersQueryHandler {

    @Autowired
    private CourierRepository repository;

    @Override
    public Result<GetCouriersResponse, Error> handle() {
        var couriers = GetCouriersResponse.create(repository.getAllCouriers().stream().map(courier -> new CourierDto(courier.getCourierId(), courier.getName(), courier.getLocation())).toList());
        return Result.success(couriers.getValue());
    }
}
