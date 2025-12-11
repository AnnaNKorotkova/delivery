package org.raif.delivery.core.application.queries.dto;

import org.raif.delivery.core.domain.kernel.Location;

import java.util.UUID;

public record OrderDto(
        UUID orderId,
        Location location
) {
}
