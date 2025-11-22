package org.raif.delivery.core.domain.model.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.libs.ddd.Aggregate;
import org.raif.delivery.libs.errs.Error;
import org.raif.delivery.libs.errs.Result;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Order extends Aggregate<UUID> {

    private UUID id;
    private Location location;
    private int volume;
    private OrderStatus status;
    private UUID courierId;

    private Order(UUID id, Location location, int volume, OrderStatus status, UUID courierId) {
        this.id = id;
        this.location = location;
        this.volume = volume;
        this.status = status;
        this.courierId = courierId;
    }

    public static Result<Order, Error> create(UUID id, Location location, int volume) {
        if (id == null) {
            return Result.failure(Error.of("order.validation.error", "ID could not be null"));
        }
        if (location == null) {
            return Result.failure(Error.of("order.validation.error", "location could not be null"));
        }
        if (volume == 0) {
            return Result.failure(Error.of("order.validation.error", "volume could not be 0"));
        }
        return Result.success(new Order(id, location, volume, OrderStatus.CREATED, null));
    }

    public Result<Order, Error> assignCourier(UUID courierId) {
        if(courierId == null){
            return Result.failure(Error.of("order.validation.error", "CourierId could not be null"));
        }
        this.courierId = courierId;
        this.status = OrderStatus.ASSIGNED;
        return Result.success(this);
    }

    public Result<Order, Error> terminateOrder(UUID id) {
        if (id == null) {
            return Result.failure(Error.of("order.validation.error", "ID could not be null"));
        }
        if(this.status == OrderStatus.COMPLETED || this.status == OrderStatus.CREATED){
            return Result.failure(Error.of("order.validation.error", "status should be ASSIGNED"));
        }
        this.status = OrderStatus.COMPLETED;
        return Result.success(this);
    }
}
