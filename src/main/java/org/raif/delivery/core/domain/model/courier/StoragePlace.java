package org.raif.delivery.core.domain.model.courier;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.raif.delivery.libs.ddd.BaseEntity;
import org.raif.delivery.libs.errs.Error;
import org.raif.delivery.libs.errs.Result;

import java.util.UUID;

@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class StoragePlace extends BaseEntity<UUID> {

    @Getter
    private UUID Id;

    @Getter
    private String name;

    @Getter
    private Integer totalVolume;

    @Getter
    private UUID orderId;

    private static int MIN_VOLUME = 0;

    private StoragePlace(UUID uuid, String name, Integer totalVolume, UUID orderId) {
        this.Id = uuid;
        this.name = name;
        this.totalVolume = totalVolume;
        this.orderId = orderId;
    }


    public static Result<StoragePlace, Error> create(String name, Integer totalVolume, UUID orderId) {
        if (name.isBlank()) {
            return Result.failure(Error.of("invalid.store.place", "name cant be null or empty"));
        }
        if (totalVolume <= MIN_VOLUME) {
            return Result.failure(Error.of("invalid.store.place", "total volume cant be less than 0"));
        }
        return Result.success(new StoragePlace(UUID.randomUUID(), name, totalVolume, orderId));
    }

    public boolean checkStorageVolume(Integer volume) {
        return orderId == null && volume < totalVolume;
    }

    public Result<StoragePlace, Error> putOrder(UUID orderId, int volume) {
        if (volume > totalVolume) {
            return Result.failure(Error.of("invalid.put.store.place", "volume cant be greater than totalVolume"));
        }
        if (this.orderId != null) {
            return Result.failure(Error.of("invalid.put.store.place", "storage not empty"));
        }
        this.orderId = orderId;
        return Result.success(this);
    }

    public StoragePlace extractOrder() {
        this.orderId = null;
        return this;
    }
}
