package org.raif.delivery.core.domain.services;

import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.domain.model.order.Order;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


class OrderDispatcherImplTest extends BaseTest {

    @Test
    public void shouldReturnCompatibleCourier() {
        var service = new OrderDispatcherImpl();
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(5, 6).getValue(), 5).getValue();
        var courier1 = Courier.create("Иван", 2, Location.create(1, 1).getValue()).getValue();
        var courier2 = Courier.create("Василий", 2, Location.create(4, 6).getValue()).getValue();

        var expected = service.dispatchOrder(order, List.of(courier1, courier2));

        assertTrue(expected.isSuccess());
        assertThat(expected.getValue()).isEqualTo(courier2);
    }


    @Test
    public void shouldReturnCompatibleCourierFromTwoEqualsCouriers() {
        var service = new OrderDispatcherImpl();
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(1, 1).getValue(), 5).getValue();
        var courier1 = Courier.create("Иван", 2, Location.create(1, 1).getValue()).getValue();
        var courier2 = Courier.create("Иван", 2, Location.create(1, 1).getValue()).getValue();

        var expected = service.dispatchOrder(order, List.of(courier1, courier2));

        assertTrue(expected.isSuccess());
        assertThat(expected.getValue()).isEqualTo(courier1);
    }

    @Test
    public void shouldReturnErrorCourierBecauseHeCantTakeOrder() {
        var service = new OrderDispatcherImpl();
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(5, 6).getValue(), 1000).getValue();
        var courier1 = Courier.create("Иван", 2, Location.create(1, 1).getValue()).getValue();
        var courier2 = Courier.create("Василий", 2, Location.create(4, 6).getValue()).getValue();

        var expected = service.dispatchOrder(order, List.of(courier1, courier2));

        assertTrue(expected.isFailure());
        assertThat(expected.getError().getCode()).isEqualTo("no.courier");
    }

    @Test
    public void shouldReturnCompatibleCourierFromOneCourier() {
        var service = new OrderDispatcherImpl();
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(5, 6).getValue(), 5).getValue();
        var courier1 = Courier.create("Иван", 2, Location.create(1, 1).getValue()).getValue();

        var expected = service.dispatchOrder(order, List.of(courier1));

        assertTrue(expected.isSuccess());
    }

    @Test
    public void shouldReturnErrorFromEmptyCourierList() {
        var service = new OrderDispatcherImpl();
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(5, 6).getValue(), 5).getValue();

        var expected = service.dispatchOrder(order, emptyList());

        assertTrue(expected.isFailure());
        assertThat(expected.getError().getCode()).isEqualTo("no.courier");
    }
}