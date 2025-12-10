package org.raif.delivery.adapters.out.postgres;

import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.raif.delivery.core.ports.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Testcontainers
class OrderRepositoryImplTest extends BaseTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveNewOrder() {
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(1, 1).getValue(), 5).getValue();

        orderRepository.save(order);

        var found = orderRepository.findByOrderId(orderId);
        assertThat(found).isPresent();
    }

    @Test
    void shouldUpdateOrder() {
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(1, 1).getValue(), 5).getValue();
        orderRepository.save(order);
        var courierId = UUID.randomUUID();
        order.assignCourier(courierId);
        orderRepository.update(order);

        var found = orderRepository.findByOrderId(orderId);

        assertThat(found).isPresent();
        assertThat(found.get().getCourierId()).isEqualTo(courierId);
    }


    @Test
    void shouldFindOrderByCreatedStatus() {
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(1, 1).getValue(), 5).getValue();
        orderRepository.save(order);

        var found = orderRepository.findByStatus(OrderStatus.CREATED);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.getFirst().getOrderId()).isEqualTo(orderId);
    }


    @Test
    void shouldNotFindOrderByAssignedStatus() {
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(1, 1).getValue(), 5).getValue();
        orderRepository.save(order);

        var found = orderRepository.findByStatus(OrderStatus.ASSIGNED);

        assertThat(found.size()).isEqualTo(0);
    }

    @Test
    void shouldFindOrderByAssignedStatus() {
        var orderId = UUID.randomUUID();
        var courierId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(1, 1).getValue(), 5).getValue();
        order.assignCourier(courierId);
        orderRepository.save(order);

        var found = orderRepository.findByAssignedStatus();

        assertThat(found.size()).isEqualTo(1);
    }
}