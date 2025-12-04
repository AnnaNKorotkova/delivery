package org.raif.delivery.core.application.queries.dto;

import org.raif.delivery.core.domain.kernal.Location;

import java.util.UUID;

public record OrderDto(
        UUID orderId,
        Location location
) {
}
