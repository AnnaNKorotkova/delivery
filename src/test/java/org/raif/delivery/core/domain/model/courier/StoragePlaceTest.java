package org.raif.delivery.core.domain.model.courier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StoragePlaceTest {

    @Test
    public void createEmptyStorage() {
        var name = "рюкзак";
        var totalVolume = 10;

        var actual = StoragePlace.create(name, totalVolume, null);

        assertThat(actual).isNotNull();
        assertThat(actual.isFailure()).isFalse();
        assertThat(actual.getValue().getName()).isEqualTo(name);
        assertThat(actual.getValue().getTotalVolume()).isEqualTo(totalVolume);
    }

    @CsvSource(delimiter = ';',
            value = {
                    "'';10",
                    "рюкзак;0",
                    "рюкзак;-1"
            }
    )
    @ParameterizedTest(name = "Создание недопустимого хранилища с name={0} и totalVolume={1}")
    public void createInvalidStorage(String name, int totalVolume) {

        var actual = StoragePlace.create(name, totalVolume, null);

        assertThat(actual.isFailure()).isTrue();
        assertThat(actual.getError().getCode()).isEqualTo("invalid.store.place");
    }

    @Test
    public void shouldReturnTrueWhenStorageEmptyAndCorrectVolume() {
        var name = "рюкзак";
        var totalVolume = 10;
        var place = StoragePlace.create(name, totalVolume, null).getValue();

        var actual = place.checkStorageVolume(5);

        assertThat(actual).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenStorageNotEmpty() {
        var name = "рюкзак";
        var totalVolume = 10;
        var orderId = UUID.randomUUID();
        var place = StoragePlace.create(name, totalVolume, orderId).getValue();

        var actual = place.checkStorageVolume(5);

        assertThat(actual).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenStorageTooSmall() {
        var name = "рюкзак";
        var totalVolume = 10;
        var orderId = UUID.randomUUID();
        var place = StoragePlace.create(name, totalVolume, orderId).getValue();

        var actual = place.checkStorageVolume(15);

        assertThat(actual).isFalse();
    }

    @Test
    public void shouldPutCorrectOrderIntoEmptyStorage() {
        var name = "рюкзак";
        var totalVolume = 10;
        var orderId = UUID.randomUUID();
        var place = StoragePlace.create(name, totalVolume, null).getValue();

        var actual = place.putOrder(orderId, 5);

        assertThat(actual.isSuccess()).isTrue();
        assertThat(actual.getValue().getOrderId()).isEqualTo(orderId);
    }

    @Test
    public void shouldNotPutCorrectOrderIntoFullStorage() {
        var name = "рюкзак";
        var totalVolume = 10;
        var orderIdIntoStorage = UUID.randomUUID();
        var orderId = UUID.randomUUID();
        var place = StoragePlace.create(name, totalVolume, orderIdIntoStorage).getValue();

        var actual = place.putOrder(orderId, 5);

        assertThat(actual.isFailure()).isTrue();
        assertThat(actual.getError().getCode()).isEqualTo("invalid.put.store.place");
    }

    @Test
    public void shouldNotPutCorrectOrderIntoSmallStorage() {
        var name = "рюкзак";
        var totalVolume = 10;
        var orderId = UUID.randomUUID();
        var place = StoragePlace.create(name, totalVolume, null).getValue();

        var actual = place.putOrder(orderId, 15);

        assertThat(actual.isFailure()).isTrue();
        assertThat(actual.getError().getCode()).isEqualTo("invalid.put.store.place");
    }


    @Test
    public void shouldExtractOrderFromStorage() {
        var name = "рюкзак";
        var totalVolume = 10;
        var orderId = UUID.randomUUID();
        var place = StoragePlace.create(name, totalVolume, orderId).getValue();

        var actual = place.extractOrder();

        assertThat(actual.getOrderId()).isNull();
    }

}