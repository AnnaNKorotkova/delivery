package org.raif.delivery.adapters.in.http;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.raif.delivery.adapters.in.http.api.ApiApi;
import org.raif.delivery.adapters.in.http.model.Courier;
import org.raif.delivery.adapters.in.http.model.Error;
import org.raif.delivery.adapters.in.http.model.Location;
import org.raif.delivery.adapters.in.http.model.NewCourier;
import org.raif.delivery.adapters.in.http.model.Order;
import org.raif.delivery.core.application.queries.GetAllCouriersQueryHandler;
import org.raif.delivery.core.application.queries.GetUnfinishedOrdersQueryHandler;
import org.raif.delivery.core.application.сommands.CreateCourierCommand;
import org.raif.delivery.core.application.сommands.CreateCourierCommandHandler;
import org.raif.delivery.core.application.сommands.CreateOrderCommand;
import org.raif.delivery.core.application.сommands.CreateOrderCommandHandler;
import org.raif.libs.errs.UnitResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
public class DeliveryController implements ApiApi {
    @Autowired
    private CreateCourierCommandHandler createCourierCommandHandler;

    @Autowired
    private CreateOrderCommandHandler createOrderCommandHandler;

    @Autowired
    private GetUnfinishedOrdersQueryHandler getUnfinishedOrdersQueryHandler;

    @Autowired
    private GetAllCouriersQueryHandler getAllCouriersQueryHandler;


    @Override
    public ResponseEntity<Void> createCourier(NewCourier newCourier) {

        var command = new CreateCourierCommand(newCourier.getName(), newCourier.getSpeed());

        UnitResult<org.raif.libs.errs.Error> handle = createCourierCommandHandler.handle(command);
        if (handle.isFailure()) {
            throw new ResponseExceptions(400, handle.getError().getMessage());
        }
        return new ResponseEntity<>(CREATED);
    }

    @Override
    public ResponseEntity<Void> createOrder() {
        var command = new CreateOrderCommand(UUID.randomUUID(), "Ленина", 10);

        var handle = createOrderCommandHandler.handle(command);
        if (handle.isFailure()) {
            throw new ResponseExceptions(400, handle.getError().getMessage());
        }
        return new ResponseEntity<>(CREATED);
    }

    @Override
    public ResponseEntity<List<Courier>> getCouriers() {
        var couriers = getAllCouriersQueryHandler.handle().getValue().couriers().stream()
                .map(courier -> new Courier(courier.courierId(), courier.name(), new Location(courier.location().getX(), courier.location().getY()))).toList();
        return ResponseEntity.ok(couriers);
    }

    @Override
    public ResponseEntity<List<Order>> getOrders() {
        var orders = getUnfinishedOrdersQueryHandler.handle().getValue().orders().stream()
                .map(orderDto -> new Order(orderDto.orderId(), new Location(orderDto.location().getX(), orderDto.location().getY()))).toList();
        return ResponseEntity.ok(orders);
    }

    @ExceptionHandler(ResponseExceptions.class)
    public ResponseEntity<org.raif.delivery.adapters.in.http.model.Error> handleValidationException(ResponseExceptions ex) {
        Error error = new Error(400, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
