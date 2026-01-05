package com.incident.controller;

import com.incident.dto.EventRequest;
import com.incident.kafka.EventProducer;
// import com.incident.entity.Event;
// import com.incident.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    
    /*
        // Before async introduction i used this:
        private final EventService eventService;
        // Without Lombok:
        public EventController(EventService eventService) {
            this.eventService = eventService;
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Event createEvent(@Valid @RequestBody EventRequest request) {
            return eventService.createEvent(request);
        }
     */

        
    private final EventProducer eventProducer;
    /*
        // Without Lombok:
        public EventController(EventProducer eventProducer) {
            this.eventProducer = eventProducer;
        }
     */

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED) // 202 Accepted because async
    public Map<String, String> ingestEvent(@Valid @RequestBody EventRequest request) {
        eventProducer.publishEvent(request);
        return Map.of("status", "QUEUED"); //I am returning a Map, because it provides a quick way to return small JSON response without creating a new class. 
    }
}