package org.raif.delivery.core.application.queries.dto;

import java.util.UUID;
import org.raif.delivery.core.domain.kernal.Location;

public record CourierDto(
        UUID courierId,
        String name,
        Location location
) {
}
