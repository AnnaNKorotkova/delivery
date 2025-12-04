package org.raif.delivery.core.application.сommands;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.ports.OrderRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreateOrderCommandHandlerImplTest extends BaseTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Test
    void shouldCreateOrder() {
        var orderId = UUID.randomUUID();
        var handler = new CreateOrderCommandHandlerImpl(orderRepository);
        var command = CreateOrderCommand.create(orderId,"Ленина", 5).getValue();

        handler.handle(command);

        verify(orderRepository).save(any(Order.class));
    }

}