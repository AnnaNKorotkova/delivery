package org.raif.delivery.core.ports;

public interface UnitOfWork {
    AggregateTracker getAggregateTracker();
    void commit();
}