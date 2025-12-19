package org.raif.delivery.core.domain.events;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.domain.model.order.Order;
import org.raif.delivery.core.domain.model.order.OrderStatus;
import org.raif.libs.ddd.DomainEvent;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class OrderCompletedDomainEvent extends DomainEvent {
    private final UUID orderId;
    private final Location location;
    private final int volume;
    private final OrderStatus status;
    private final UUID courierId;

    public OrderCompletedDomainEvent(Order order) {
        super(order);
        this.orderId = order.getId();
        this.location = order.getLocation();
        this.volume = order.getVolume();
        this.status = order.getStatus();
        this.courierId = order.getOrderId();
    }
}
