package org.raif.delivery.core.domain.kernal;


import org.raif.delivery.libs.ddd.ValueObject;
import org.raif.delivery.libs.errs.Error;
import org.raif.delivery.libs.errs.Result;

import java.util.List;

public class Location extends ValueObject<Location> {
    private final int x;
    private final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public static Result<Location, Error> create(int x, int y) {
        if (x <= 0 || y <= 0 || x > 10 || y > 10) {
            return Result.failure(Error.of("invalid.coordinate", "Coordinates must be positive with max value 10."));
        }
        return Result.success(new Location(x, y));
    }

    public Integer distance(Location location) {
        return Math.abs((getX()-location.x) + (getY()-location.y));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(x, y);
    }
}
