package org.raif.delivery.core.domain.kernal;


import org.raif.delivery.libs.ddd.ValueObject;
import org.raif.delivery.libs.errs.Error;
import org.raif.delivery.libs.errs.Result;

import java.util.List;

public class Location extends ValueObject<Location> {
    private final int x;
    private final int y;
    private static final int MIN_COORDINATE = 0;
    private static final int MAX_COORDINATE = 10;

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
        if (x <= MIN_COORDINATE || y <= MIN_COORDINATE || x > MAX_COORDINATE || y > MAX_COORDINATE) {
            return Result.failure(Error.of("invalid.coordinate", "Coordinates must be positive with max value 10."));
        }
        return Result.success(new Location(x, y));
    }

    public Integer distance(Location location) {
        if (location == null) {
            throw new NullPointerException("location must not be null.");
        }
        return Math.abs((getX() - location.x) + (getY() - location.y));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(x, y);
    }
}
