package com.incident.controller;

import com.incident.dto.EventRequest;
import com.incident.entity.Event;
import com.incident.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    /*
        // Without Lombok:
        public EventController(EventService eventService) {
            this.eventService = eventService;
        }
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@Valid @RequestBody EventRequest request) {
        return eventService.createEvent(request);
    }
}