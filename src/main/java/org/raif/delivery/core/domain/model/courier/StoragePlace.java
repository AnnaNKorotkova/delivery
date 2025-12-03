package org.raif.delivery.core.domain.model.courier;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.raif.libs.ddd.BaseEntity;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;

import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoragePlace extends BaseEntity<UUID> {

    @Column(name = "storage_place_id")
    private UUID storagePlaceId;

    @Column(name = "storage_place_name")
    private String name;

    @Column(name = "storage_place_total_volume")
    private Integer totalVolume;

    @Column(name = "order_id")
    private UUID orderId;

    public StoragePlace(UUID uuid, String name, Integer totalVolume, UUID orderId) {
        super(uuid);
        this.storagePlaceId = uuid;
        this.name = name;
        this.totalVolume = totalVolume;
        this.orderId = orderId;
    }

    public static Result<StoragePlace, Error> create(String name, Integer totalVolume, UUID orderId) {
        if (name.isBlank()) {
            return Result.failure(Error.of("invalid.store.place", "name cant be null or empty"));
        }
        int MIN_VOLUME = 0;
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
