package org.raif.delivery.core.ports;

public interface OrderEventProducer<T> {
    void publish(T event) throws Exception;
}
