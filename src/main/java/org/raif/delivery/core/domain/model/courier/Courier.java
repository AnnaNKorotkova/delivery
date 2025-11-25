package org.raif.delivery.core.domain.model.courier;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.libs.ddd.Aggregate;
import org.raif.delivery.libs.ddd.BaseEntity;
import org.raif.delivery.libs.errs.Error;
import org.raif.delivery.libs.errs.GeneralErrors;
import org.raif.delivery.libs.errs.Result;
import org.raif.delivery.libs.errs.UnitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Courier extends Aggregate<UUID> {
    private UUID id;
    private String name;
    private Location location;
    private int speed;
    private List<StoragePlace> storagePlaces;

    private Courier(UUID id, String name, Location location, int speed, List<StoragePlace> storagePlaces) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.speed = speed;
        this.storagePlaces = storagePlaces;
    }

    public static Result<Courier, Error> create(String name, int speed, Location location) {
        if (name == null || name.isBlank()) {
            return Result.failure(Error.of("invalid.courier", "name cant be null or empty"));
        }
        if (speed == 0) {
            return Result.failure(Error.of("invalid.courier", "speed cant be 0"));
        }
        if (location == null) {
           throw new IllegalArgumentException("location cant be null");
        }
        var storage = new ArrayList<StoragePlace>();
        storage.add(StoragePlace.create("Сумка", 10, null).getValue());
        return Result.success(new Courier(UUID.randomUUID(), name, location, speed, storage));
    }

    public Result<Courier, Error> addStoragePlace(String name, int totalVolume) {
        if (name == null || totalVolume == 0 || name.isBlank()) {
            return Result.failure(Error.of("order.validation.error", "StoragePlace should have name  and total"));
        }
        var storage = StoragePlace.create(name,totalVolume, null).getValue();
        this.storagePlaces.add(storage);
        return Result.success(this);
    }

    public Result<Boolean,Error> isPlaceForNewOrder(Order order) {
        if(order == null) {
            return Result.failure(Error.of("order.validation.error", "Order could not be null"));
        }
        return Result.success(!getFreeStoragePlace(order).isEmpty());
    }

    public Result<Courier, Error> takeOrder(Order order) {
        if (!isPlaceForNewOrder(order).getValue()) {
            return Result.failure(Error.of("order.take.error", "Not enough storage places"));
        }
        List<StoragePlace> freeStoragePlace = getFreeStoragePlace(order);
        freeStoragePlace.getFirst().putOrder(order.getId(), order.getVolume());
        return Result.success(this);
    }

    public Result<Courier, Error> terminateOrder(Order order) {
        if (order == null) {
            return Result.failure(Error.of("order.validation.error", "Order cant be null"));
        }
        var storage = this.storagePlaces.stream()
                .filter(storagePlace -> storagePlace.getOrderId() == order.getId())
                .findFirst().orElse(null);
        if (storage == null) {
            return Result.failure(Error.of("order.validation.error", "Order from another courier "));
        }
        storage.extractOrder();
        return Result.success(this);
    }


    public Result<Integer, Error> countStepSpeedCourier(Location target) {
        if (target == null) {
            return Result.failure(Error.of("order.validation.error", "Target cant be null"));
        }
        return Result.success(this.location.distance(target) / speed);
    }

    public UnitResult<Error> move(Location target) {
        if (target == null) {
            return UnitResult.failure(GeneralErrors.valueIsRequired("target"));
        }

        int difX = target.getX() - location.getX();
        int difY = target.getY() - location.getY();
        int cruisingRange = speed;

        int moveX = Math.max(-cruisingRange, Math.min(difX, cruisingRange));
        cruisingRange -= Math.abs(moveX);

        int moveY = Math.max(-cruisingRange, Math.min(difY, cruisingRange));

        Result<Location, Error> locationCreateResult = Location.create(
                location.getX() + moveX,
                location.getY() + moveY
        );

        if (locationCreateResult.isFailure()) {
            return UnitResult.failure(locationCreateResult.getError());
        }

        this.location = locationCreateResult.getValue();
        return UnitResult.success();
    }

    private List<StoragePlace> getFreeStoragePlace(Order order) {
        return this.storagePlaces.stream()
                .filter(storagePlace ->
                        storagePlace.getOrderId() == null && storagePlace.getTotalVolume() >= order.getVolume()).toList();
    }

    @Override
    public int compareTo(BaseEntity<UUID> other) {
        return super.compareTo(other);
    }
}
