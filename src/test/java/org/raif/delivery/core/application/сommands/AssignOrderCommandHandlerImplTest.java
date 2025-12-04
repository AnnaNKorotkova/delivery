package org.raif.delivery.core.application.сommands;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.raif.delivery.core.ports.CourierRepository;
import org.raif.delivery.core.ports.OrderRepository;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AssignOrderCommandHandlerImplTest extends BaseTest {

    private final CourierRepository courierRepository = mock(CourierRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);


    @Test
    public void shouldAssignOrder() {
        var orderId = UUID.randomUUID();
        var courier = Courier.create("Иван", 1, Location.create(1,1).getValue()).getValue();
        var order = Order.create(orderId, Location.create(5,6).getValue(), 3).getValue();
        var command = AssignOrderCommand.create().getValue();
        var handler = new AssignOrderCommandHandlerImpl(courierRepository,orderRepository);
        when(courierRepository.findFreeCouriers()).thenReturn(List.of(courier));
        when(orderRepository.findByStatus(OrderStatus.CREATED)).thenReturn(List.of(order));

        var handle = handler.handle(command);

        Assertions.assertTrue(handle.isSuccess());
        verify(courierRepository).save(any(Courier.class));
    }

    @Test
    public void shouldErrorWithEmptyCouriers() {
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(5,6).getValue(), 3).getValue();
        var command = AssignOrderCommand.create().getValue();
        var handler = new AssignOrderCommandHandlerImpl(courierRepository,orderRepository);
        when(courierRepository.findFreeCouriers()).thenReturn(emptyList());
        when(orderRepository.findByStatus(OrderStatus.CREATED)).thenReturn(List.of(order));

        var handle = handler.handle(command);

        Assertions.assertTrue(handle.isFailure());
        verify(courierRepository, never()).save(any(Courier.class));
    }
    @Test
    public void shouldErrorWithEmptyOrders() {
        var orderId = UUID.randomUUID();
        var courier = Courier.create("Иван", 1, Location.create(1,1).getValue()).getValue();
        var command = AssignOrderCommand.create().getValue();
        var handler = new AssignOrderCommandHandlerImpl(courierRepository,orderRepository);
        when(courierRepository.findFreeCouriers()).thenReturn(List.of(courier));
        when(orderRepository.findByStatus(OrderStatus.CREATED)).thenReturn(emptyList());

        var handle = handler.handle(command);

        Assertions.assertTrue(handle.isFailure());
        verify(courierRepository, never()).save(any(Courier.class));
    }
}