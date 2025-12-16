package org.raif.delivery.core.domain.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.libs.ddd.Aggregate;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Order extends Aggregate<UUID> {
    @Column(name = "order_id")
    private UUID orderId;
    @Embedded
    private Location location;
    @Column(name = "volume")
    private int volume;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "courier_id")
    private UUID courierId;

    public UUID getStoragePlaceId() {
        return orderId;
    }

    private Order(UUID id, UUID orderId, Location location, int volume, OrderStatus status, UUID courierId) {
        super(id);
        this.orderId = orderId;
        this.location = location;
        this.volume = volume;
        this.status = status;
        this.courierId = courierId;
    }

    public static Result<Order, Error> create(UUID courierId, Location location, int volume) {
        if (courierId == null) {
            return Result.failure(Error.of("order.validation.error", "ID could not be null"));
        }
        if (location == null) {
            return Result.failure(Error.of("order.validation.error", "location could not be null"));
        }
        if (volume == 0) {
            return Result.failure(Error.of("order.validation.error", "volume could not be 0"));
        }
        return Result.success(new Order(UUID.randomUUID(), courierId, location, volume, OrderStatus.CREATED, null));
    }

    public Result<Order, Error> assignCourier(UUID courierId) {
        if (courierId == null) {
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
        if (this.status == OrderStatus.COMPLETED || this.status == OrderStatus.CREATED) {
            return Result.failure(Error.of("order.validation.error", "status should be ASSIGNED"));
        }
        this.status = OrderStatus.COMPLETED;
        return Result.success(this);
    }

    public Result<Order, Error> terminateOrder() {
        if (this.status == OrderStatus.COMPLETED || this.status == OrderStatus.CREATED) {
            return Result.failure(Error.of("order.validation.error", "status should be ASSIGNED"));
        }
        this.status = OrderStatus.COMPLETED;
        return Result.success(this);
    }
}

