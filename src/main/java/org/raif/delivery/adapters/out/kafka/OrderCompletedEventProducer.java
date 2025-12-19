package org.raif.delivery.adapters.out.kafka;

import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.raif.delivery.core.domain.events.OrderCompletedDomainEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import queues.order.OrderEventsProto.OrderCompletedIntegrationEvent;

@Component
@RequiredArgsConstructor
public class OrderCompletedEventProducer implements org.raif.delivery.core.ports.OrderEventProducer<OrderCompletedDomainEvent> {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.order-events-topic}")
    private String topic;

    @Override
    @SneakyThrows
    public void publish(OrderCompletedDomainEvent event) {
        OrderCompletedIntegrationEvent integrationEvent = OrderCompletedIntegrationEvent.newBuilder()
                .setEventId(event.getEventId().toString())
                .setEventType(event.getClass().getSimpleName())
                .setOrderId(event.getOrderId().toString())
                .setCourierId(event.getCourierId().toString())
                .build();
        var integrationEventAsJson = JsonFormat.printer().includingDefaultValueFields().print(integrationEvent);
        kafkaTemplate.send(topic, event.getOrderId().toString(), integrationEventAsJson).get();
    }

}
