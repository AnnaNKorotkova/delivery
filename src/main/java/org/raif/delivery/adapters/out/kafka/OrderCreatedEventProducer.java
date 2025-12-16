package org.raif.delivery.adapters.out.kafka;

import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.raif.delivery.core.domain.events.OrderCreatedDomainEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import queues.order.OrderEventsProto.OrderCreatedIntegrationEvent;

@Component
@RequiredArgsConstructor
public class OrderCreatedEventProducer implements org.raif.delivery.core.ports.OrderEventProducer<OrderCreatedDomainEvent> {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.order-events-topic}")
    private String topic;

    @Override
    @SneakyThrows
    public void publish(OrderCreatedDomainEvent event){
        OrderCreatedIntegrationEvent integrationEvent = OrderCreatedIntegrationEvent.newBuilder()
                .setEventId(event.getEventId().toString())
                .setEventType(event.getClass().getSimpleName())
                .setOrderId(event.getOrderId().toString())
                .build();
        var integrationEventAsJson = JsonFormat.printer().includingDefaultValueFields().print(integrationEvent);
        kafkaTemplate.send(topic, event.getOrderId().toString(), integrationEventAsJson).get();
    }

}
