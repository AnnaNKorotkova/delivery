package org.raif.delivery.adapters.in.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.util.JsonFormat;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.raif.delivery.core.application.commands.CreateOrderCommand;
import org.raif.delivery.core.application.commands.CreateOrderCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import queues.basket.BasketEventsProto;

@Service
@RequiredArgsConstructor
public class BasketEventsConsumer {
    private static final Logger log = LoggerFactory.getLogger(BasketEventsConsumer.class);
    private final CreateOrderCommandHandler createOrderCommandHandler;

    @KafkaListener(topics = "${app.kafka.basket-events-topic}")
    public void listen(String message) {
        log.info("Raw event received: {}", message);

        try {
            String fixedJson = fixTimestamp(message);
            // Десериализация
            var builder = BasketEventsProto.BasketConfirmedIntegrationEvent.newBuilder();
            JsonFormat.parser().merge(fixedJson, builder);
            var event = builder.build();

            // Создаем команду
            var createCommandResult = CreateOrderCommand.create(UUID.fromString(event.getBasketId()), event.getAddress().getStreet(), event.getVolume());
            if (createCommandResult.isFailure()) {
                throw new RuntimeException("Invalid command: " + createCommandResult.getError());
            }
            var command = createCommandResult.getValue();

            // Обрабатываем команду
            var handleCommandResult = this.createOrderCommandHandler.handle(command);
            if (handleCommandResult.isFailure()) {
                throw new RuntimeException("Failed to handle command: " + handleCommandResult.getError());
            }

        } catch (com.google.protobuf.InvalidProtocolBufferException ex) {
            throw new RuntimeException("Failed to parse protobuf message", ex);
        }
    }

    private String fixTimestamp(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(json);

            JsonNode timestampNode = node.get("occurredAt");
            if (timestampNode != null && timestampNode.isObject()) {
                long seconds = timestampNode.get("seconds").asLong();
                int nanos = timestampNode.get("nanos").asInt();
                Instant instant = Instant.ofEpochSecond(seconds, nanos);
                String isoTimestamp = instant.toString();

                ((ObjectNode) node).put("occurredAt", isoTimestamp);

                return mapper.writeValueAsString(node);
            }
            return json;

        } catch (Exception e) {
            log.warn("Could not fix occurredAt: {}", e.getMessage());
            return json;
        }
    }
}