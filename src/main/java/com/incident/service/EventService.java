package com.incident.service;

import com.incident.dto.EventRequest;
import com.incident.entity.Event;
import com.incident.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service; //Marks the class as a service component.

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    /*
        // without @RequiredArgsConstructor we need to inject constructor manually
        public EventService(EventRepository eventRepository){
            this.eventRepository = eventRepository;
        }
    */

    public Event createEvent(EventRequest request) { //Method to take data from DTO
        Event event = Event.builder()
                .serviceName(request.getServiceName())
                .eventType(request.getEventType())
                .severity(request.getSeverity())
                .message(request.getMessage())
                .metadata(request.getMetadata())
                .occurredAt(request.getOccurredAt() != null ? request.getOccurredAt() : Instant.now()) //If client sends timeStampwe can use it, else uses system time.
                .ingestedAt(Instant.now()) //to record when system received this event. Event if the event happened earlier.
                .build();

        return eventRepository.save(event);
    }
}

/*
    // Again, without lombok we cannot use builder and required to write manually.
    Event event = new Event();
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