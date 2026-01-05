package com.incident.kafka;

import tools.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // Spring Boot auto-provides this ObjectMapper bean(jackson).

        // Constructor (without Lombok)
        public EventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
            this.kafkaTemplate = kafkaTemplate;
            this.objectMapper = objectMapper;
        }

    public void publishEvent(Object eventRequest) {
        try {
            String json = objectMapper.writeValueAsString(eventRequest); // convers java object in to json text

            // Key- matters later for ordering per service, right now no key gives simpler and faster ingestion
            // Here we are sending only topic and value (no key). Kafka will choose partitions in a round-robin
            kafkaTemplate.send(KafkaTopics.EVENTS_TOPIC, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize/publish event to Kafka", e);
        }
    }
}
