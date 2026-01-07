package com.incident.kafka;

import tools.jackson.databind.ObjectMapper;
import com.incident.dto.EventRequest;
import com.incident.entity.Event;
import com.incident.repository.EventRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final ObjectMapper objectMapper;
    private final EventRepository eventRepository;

    /*
        Without Lombok-  @RequiredArgsConstructor
        public EventConsumer(ObjectMapper objectMapper, EventRepository eventRepository){
            this.objectMapper = objectMapper;
            this.eventRepository = eventRepository;
        }
    
     */

    @KafkaListener(topics = KafkaTopics.EVENTS_TOPIC) // It tells spring whenever a message is arrrived from this kafka topic, call this method (consume).
    public void consume(String messageJson) throws Exception{

        EventRequest request = objectMapper.readValue(messageJson, EventRequest.class); // JSON we got from producer and changing it to Java Object.

        //Interntional failure trigger to test retries + DLT
        //If message contains "FAIL", throw exception
        //if(request.getMessage() != null && request.getMessage().toUpperCase().contains("FAIL")){
        //    throw new RuntimeException("Testing retries + DLT failures");
        //}

        // Building DB Entity
        Event event = Event.builder()
            .eventId(request.getEventId())
            .serviceName(request.getServiceName())
            .eventType(request.getEventType())
            .severity(request.getSeverity())
            .message(request.getMessage())
            .metadata(request.getMetadata())
            .occurredAt(request.getOccurredAt() != null ? request.getOccurredAt() : Instant.now())
            .ingestedAt(Instant.now())
            .build();
        try {
                eventRepository.save(event);
                System.out.println("Consumed message" + messageJson);
        } catch (DataIntegrityViolationException e) {
            // For now we just log/throw. Later we’ll add DLQ (dead-letter topic).
            //throw new RuntimeException("Failed to consume/process event message", e);
            return;
        }
    }
}

/*
    // Again, without lombok we cannot use builder and required to write manually.
    Event event = new Event();
    event.setEventId(request.getEventId());
    event.setServiceName(request.getServiceName());
    event.setEventType(request.getEventType());
    event.setSeverity(request.getSeverity());
    event.setMessage(request.getMessage());
    event.setMetadata(request.getMetadata());
        
    // Set timestamps
    if (request.getOccurredAt() != null) {
        event.setOccurredAt(request.getOccurredAt());
    } else {
        event.setOccurredAt(Instant.now());
    }

    event.setIngestedAt(Instant.now());

    return eventRepository.save(event);
 */
