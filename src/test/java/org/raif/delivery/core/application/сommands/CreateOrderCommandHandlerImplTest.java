package org.raif.delivery.core.application.сommands;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.application.commands.CreateOrderCommand;
import org.raif.delivery.core.application.commands.CreateOrderCommandHandlerImpl;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.ports.GeoClient;
import org.raif.delivery.core.ports.OrderRepository;
import org.raif.libs.errs.Result;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateOrderCommandHandlerImplTest extends BaseTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final GeoClient geoClient = mock(GeoClient.class);

    @Test
    void shouldCreateOrder() {
        var orderId = UUID.randomUUID();
        var handler = new CreateOrderCommandHandlerImpl(orderRepository,geoClient);
        var command = CreateOrderCommand.create(orderId,"Ленина", 5).getValue();
        when(geoClient.getLocation(command.street())).thenReturn(Result.success(Location.create(1, 1)).getValue());

        handler.handle(command);

        verify(orderRepository).save(any(Order.class));
    }

}