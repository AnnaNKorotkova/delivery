package org.raif.delivery.adapters.out.postgres;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.delivery.core.domain.model.courier.Courier;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.ports.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Testcontainers
class CourierRepositoryImplTest extends BaseTest {

    @Autowired
    private CourierRepository courierRepository;


    @Test
    void shouldSaveCourier() {
        var courier = Courier.create("Иван", 1, Location.create(1, 1).getValue()).getValue();

        courierRepository.save(courier);

        var found = courierRepository.findById(courier.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Иван");
        assertThat(found.get().getStoragePlaces().size()).isEqualTo(1);
        assertThat(found.get().getStoragePlaces().getFirst().getName()).isEqualTo("Сумка");
    }

    @Test
    void shouldUpdateCourier() {
        var courier = Courier.create("Иван", 1, Location.create(1, 1).getValue()).getValue();
        courierRepository.save(courier);
        courier.addStoragePlace("Рюкзак", 2);

        courierRepository.update(courier);

        var found = courierRepository.findById(courier.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Иван");
        assertThat(found.get().getStoragePlaces().size()).isEqualTo(2);
        assertThat(found.get().getStoragePlaces().getFirst().getName()).isEqualTo("Сумка");
        assertThat(found.get().getStoragePlaces().get(1).getName()).isEqualTo("Рюкзак");
    }

    @Test
    void shouldFind2FreeCouriers() {
        var courier1 = Courier.create("Иван", 1, Location.create(1, 1).getValue()).getValue();
        courierRepository.save(courier1);
        var courier2 = Courier.create("Василий", 1, Location.create(1, 1).getValue()).getValue();
        courierRepository.save(courier2);

        var found = courierRepository.findFreeCouriers();

        assertThat(found).hasSize(2);
    }


    @Test
    void shouldFind1FreeCouriers() {
        var courier1 = Courier.create("Иван", 1, Location.create(1, 1).getValue()).getValue();
        var orderId = UUID.randomUUID();
        var order = Order.create(orderId, Location.create(1, 1).getValue(), 5).getValue();
        courier1.takeOrder(order);
        courierRepository.save(courier1);
        var courier2 = Courier.create("Иван", 1, Location.create(1, 1).getValue()).getValue();
        courierRepository.save(courier2);

        var found = courierRepository.findFreeCouriers();

        assertThat(found).hasSize(1);
    }
}