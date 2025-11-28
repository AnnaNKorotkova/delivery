package org.raif.delivery;

import org.junit.jupiter.api.Test;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.libs.errs.Error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationTest extends BaseTest {

    @Test
    public void shouldCreateValidLocation() {

        var location = Location.create(1, 1);

        assertThat(location).isNotNull();
        assertThat(location.getValue().getX()).isEqualTo(1);
        assertThat(location.getValue().getY()).isEqualTo(1);
    }

    @Test
    public void shouldNotCreateInvalidMinValidLocation() {
        var expectedError = Error.of("invalid.coordinate", "Coordinates must be positive with max value 10.");

        var location = Location.create(-1, -1);

        assertThat(location).isNotNull();
        assertThat(location.isFailure()).isTrue();
        assertThat(location.getError()).isEqualTo(expectedError);
    }

    @Test
    public void shouldNotCreateInvalidZeroValidLocation() {
        var expectedError = Error.of("invalid.coordinate", "Coordinates must be positive with max value 10.");

        var location = Location.create(0, 0);

        assertThat(location).isNotNull();
        assertThat(location.isFailure()).isTrue();
        assertThat(location.getError()).isEqualTo(expectedError);
    }

    @Test
    public void shouldNotCreateInvalidMaxValidLocation() {
        var expectedError = Error.of("invalid.coordinate", "Coordinates must be positive with max value 10.");

        var location = Location.create(11, 11);

        assertThat(location).isNotNull();
        assertThat(location.isFailure()).isTrue();
        assertThat(location.getError()).isEqualTo(expectedError);
    }


    @Test
    public void shouldNotCreateInvalidMaxMinValidLocation() {
        var expectedError = Error.of("invalid.coordinate", "Coordinates must be positive with max value 10.");

        var location = Location.create(0, 11);

        assertThat(location).isNotNull();
        assertThat(location.isFailure()).isTrue();
        assertThat(location.getError()).isEqualTo(expectedError);
    }

    @Test
    public void shouldBeEqualsIfTheyCoordinatesAreTheSame() {

        var location = Location.create(1, 2);

        assertThat(location.getValue()).isEqualTo(Location.create(1, 2).getValue());
    }

    @Test
    public void shouldCalculateDistanceBetweenTwoLocations() {

        var location1 = Location.create(1, 2);
        var location2 = Location.create(3, 4);

        assertThat(location1.getValue().distance(location2.getValue())).isEqualTo(4);
    }

    @Test
    public void shouldNotBeNullInDistanceBetweenTwoLocations() {

        var location1 = Location.create(1, 2);

        assertThrows(NullPointerException.class, () -> location1.getValue().distance(null));

    }

}
