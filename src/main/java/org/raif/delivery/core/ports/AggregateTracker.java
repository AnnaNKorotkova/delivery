package org.raif.delivery.core.ports;


import org.raif.delivery.libs.ddd.AggregateRoot;

import java.util.List;

public interface AggregateTracker {
    void track(AggregateRoot aggregate);
    List<AggregateRoot> getTracked();
    void clear();
}