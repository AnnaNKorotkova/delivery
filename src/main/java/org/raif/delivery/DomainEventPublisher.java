package org.raif.delivery;


import org.raif.libs.ddd.Aggregate;

public interface DomainEventPublisher {
    void publish(Iterable<Aggregate<?>> aggregates);
}