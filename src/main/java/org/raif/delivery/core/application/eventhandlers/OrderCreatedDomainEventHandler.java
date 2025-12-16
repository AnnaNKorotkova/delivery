package org.raif.delivery.core.application.eventhandlers;

import org.raif.delivery.core.domain.events.OrderCreatedDomainEvent;
import org.raif.libs.errs.UnitResult;

interface OrderCreatedDomainEventHandler {
    UnitResult<Error> handle(OrderCreatedDomainEvent event) throws Exception;
}
