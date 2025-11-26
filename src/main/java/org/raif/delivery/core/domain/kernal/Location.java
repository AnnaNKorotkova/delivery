package org.raif.delivery.core.domain.kernal;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.raif.delivery.libs.ddd.ValueObject;
import org.raif.delivery.libs.errs.Error;
import org.raif.delivery.libs.errs.Result;

import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Location extends ValueObject<Location> {
    @Column(name = "location_x")
    private final int x;
    @Column(name = "location_y")
    private final int y;
    private static final int MIN_COORDINATE = 0;
    private static final int MAX_COORDINATE = 10;

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
