package org.raif.delivery.adapters.out.postgres;

import org.raif.delivery.core.ports.AggregateTracker;
import org.raif.delivery.libs.ddd.AggregateRoot;
import org.raif.delivery.libs.errs.Except;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AggregateTrackerImpl implements AggregateTracker {
    private final List<AggregateRoot> tracked = new ArrayList<>();

    @Override
    public void track(AggregateRoot aggregate) {
        Except.againstNull(aggregate, "aggregate");

        if (!tracked.contains(aggregate)) {
            tracked.add(aggregate);
        }
    }

    @Override
    public List<AggregateRoot> getTracked() {
        return new ArrayList<>(tracked);
    }

    @Override
    public void clear() {
        tracked.clear();
    }
}
