package org.raif.delivery.adapters.out.postgres;

import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.raif.delivery.core.ports.OrderRepository;
import org.raif.delivery.core.ports.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryImplTest extends BaseTest {
    @Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17.7")
                    .withDatabaseName("testdb")
                    .withUsername("admin")
                    .withPassword("admin");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveNewOrder() {
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(1, 1).getValue(), 5).getValue();
        orderRepository.save(order);

        var found = orderRepository.findById(orderId);
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

        var found = orderRepository.findById(orderId);

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
        assertThat(found.getFirst().getId()).isEqualTo(orderId);
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