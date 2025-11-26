package org.raif.delivery.core.domain.model.order;

import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.domain.kernal.Location;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.raif.delivery.core.domain.model.order.OrderStatus.*;

class OrderTest extends BaseTest {

    @Test
    void createValidOrder() {
        var id = UUID.randomUUID();
        var location = Location.create(1, 1).getValue();

        var result = Order.create(id, location, 10);

        assertTrue(result.isSuccess());
        assertThat(result.getValue().getStoragePlaceId()).isEqualTo(id);
        assertThat(result.getValue().getStatus()).isEqualTo(CREATED);
        assertThat(result.getValue().getCourierId()).isNull();
    }

    @Test
    void createInValidWithNUllID() {
        var location = Location.create(1, 1).getValue();

        var result = Order.create(null, location, 10);

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("order.validation.error");
    }

    @Test
    void createInValidWithNUllLocation() {
        var id = UUID.randomUUID();

        var result = Order.create(id, null, 10);

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("order.validation.error");
    }

    @Test
    void createInValidWith0Location() {
        var id = UUID.randomUUID();
        var location = Location.create(1, 1).getValue();

        var result = Order.create(id, location, 0);

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("order.validation.error");
    }

    @Test
    void assignOrder() {
        var id = UUID.randomUUID();
        var courierId = UUID.randomUUID();
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(id, location, 10).getValue();

        var result = order.assignCourier(courierId);

        assertTrue(result.isSuccess());
        assertThat(result.getValue().getCourierId()).isEqualTo(courierId);
        assertThat(result.getValue().getStatus()).isEqualTo(ASSIGNED);
    }

    @Test
    void assignOrderWithoutCourierId() {
        var id = UUID.randomUUID();
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(id, location, 10).getValue();

        var result = order.assignCourier(null);

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("order.validation.error");
    }

    @Test
    void terminateOrder() {
        var id = UUID.randomUUID();
        var courierId = UUID.randomUUID();
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(id, location, 10).getValue();
        order.assignCourier(courierId);

        var result = order.terminateOrder(id);

        assertTrue(result.isSuccess());
        assertThat(result.getValue().getStatus()).isEqualTo(COMPLETED);
    }

    @Test
    void notTerminateOrderInCreatedStatus() {
        var id = UUID.randomUUID();
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(id, location, 10).getValue();

        var result = order.terminateOrder(id);

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("order.validation.error");
    }

    @Test
    void notTerminateOrderWithoutId() {
        var id = UUID.randomUUID();
        var courierId = UUID.randomUUID();
        var location = Location.create(1, 1).getValue();
        var order  = Order.create(id, location, 10).getValue();
        order.assignCourier(courierId);

        var result = order.terminateOrder(null);

        assertTrue(result.isFailure());
        assertThat(result.getError().getCode()).isEqualTo("order.validation.error");
    }

}