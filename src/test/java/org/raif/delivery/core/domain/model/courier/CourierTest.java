package org.raif.delivery.core.domain.model.courier;


import org.junit.jupiter.api.Test;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.core.domain.model.order.Order;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CourierTest {

    @Test
    public void createValidCourier() {

        var result = Courier.create("Иван", 3, Location.create(1, 1).getValue());

        assertTrue(result.isSuccess());
        assertThat(result.getValue().getName()).isEqualTo("Иван");
        assertThat(result.getValue().getSpeed()).isEqualTo(3);
    }

    @Test
    public void createInValidCourierWithNullName() {

        var result = Courier.create(null, 3, Location.create(1, 1).getValue());

        assertTrue(result.isFailure());
        assertThat(result.getError().getMessage()).isEqualTo("name cant be null or empty");
        assertThat(result.getError().getCode()).isEqualTo("invalid.courier");
    }

    @Test
    public void createInValidCourierWithoutSpeed() {

        var result = Courier.create("Иван", 0, Location.create(1, 1).getValue());

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("invalid.courier");
        assertThat(result.getError().getMessage()).isEqualTo("speed cant be 0");
    }

    @Test
    public void createInValidCourierWithNullLocation() {

        var result = Courier.create("Иван", 3, null);

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("invalid.courier");
        assertThat(result.getError().getMessage()).isEqualTo("Location cant be null");
    }


    @Test
    public void addNewStoragePlace() {
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        var storagePlace = StoragePlace.create("рюказник", 1, null).getValue();

        var result = courier.addStoragePlace(storagePlace);

        assertTrue(result.isSuccess());
        assertThat(result.getValue().getStoragePlaces().size()).isEqualTo(2);
        assertThat(result.getValue().getStoragePlaces().get(1).getName()).isEqualTo("рюказник");
    }

    @Test
    public void courierHasPlaceForNewOrderPlace() {
        var orderId = UUID.randomUUID();
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        var storagePlace = StoragePlace.create("рюказник", 10, orderId).getValue();
        courier.addStoragePlace(storagePlace);
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(orderId, location, 1).getValue();

        var result = courier.isPlaceForNewOrder(order);

        assertTrue(result.isSuccess());
    }

    @Test
    public void courierNotHasPlaceForNewOrderPlace() {
        var orderId = UUID.randomUUID();
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        courier.getStoragePlaces().getFirst().putOrder(orderId, 3);
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(orderId, location, 20).getValue();

        var result = courier.isPlaceForNewOrder(order);

        assertFalse(result.isFailure());
    }


    @Test
    public void courierCanTakeOrder() {
        var orderId = UUID.randomUUID();
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        var location = Location.create(1, 2).getValue();
        var order  = Order.create(orderId, location, 5).getValue();

        var result = courier.takeOrder(order);

        assertTrue(result.isSuccess());
    }

    @Test
    public void courierCanNotTakeOrder() {
        var orderId = UUID.randomUUID();
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        var location = Location.create(1, 2).getValue();
        var order  = Order.create(orderId, location, 20).getValue();

        var result = courier.takeOrder(order);

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("order.take.error");
    }


    @Test
    public void courierCanTerminateOrder() {
        var orderId = UUID.randomUUID();
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        var storagePlace = StoragePlace.create("рюказник", 10, orderId).getValue();
        courier.addStoragePlace(storagePlace);
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(orderId, location, 1).getValue();

        var result = courier.terminateOrder(order);

        assertTrue(result.isSuccess());
    }

    @Test
    public void courierCanNotTerminateOrder() {
        var orderId = UUID.randomUUID();
        var otherOrderId = UUID.randomUUID();
        var courier = Courier.create("Иван", 3, Location.create(1, 1).getValue()).getValue();
        var storagePlace = StoragePlace.create("рюказник", 10, orderId).getValue();
        courier.addStoragePlace(storagePlace);
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(orderId, location, 1).getValue();
        courier.takeOrder(order);
        var otherOorder  = Order.create(otherOrderId, location, 1).getValue();

        var result = courier.terminateOrder(otherOorder);

        assertTrue(result.isFailure());
    }

    @Test
    public void countStepSpeedCourier() {
        var courier = Courier.create("Иван", 2, Location.create(1, 2).getValue()).getValue();
        var targetLocation = Location.create(3, 4).getValue();

        var result = courier.countStepSpeedCourier(targetLocation);

        assertThat(result.getValue()).isEqualTo(2);
    }

    @Test
    public void moveCourierFrom1_1to5_6() {
        var courier = Courier.create("Иван", 2, Location.create(1, 1).getValue()).getValue();
        var targetLocation  = Location.create(5, 6).getValue();

        courier.move(targetLocation);

        assertThat(courier.getLocation().getX()).isEqualTo(3);
        assertThat(courier.getLocation().getY()).isEqualTo(1);
    }
}