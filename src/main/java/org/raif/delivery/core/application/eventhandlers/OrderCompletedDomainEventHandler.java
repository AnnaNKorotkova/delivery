package org.raif.delivery.core.application.eventhandlers;

import org.raif.delivery.core.domain.events.OrderCompletedDomainEvent;
import org.raif.libs.errs.UnitResult;

interface OrderCompletedDomainEventHandler {
    UnitResult<Error> handle(OrderCompletedDomainEvent event) throws Exception;
}
