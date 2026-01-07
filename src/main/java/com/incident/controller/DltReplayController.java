package com.incident.controller;

import com.incident.service.DltReplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dlt")
@RequiredArgsConstructor
public class DltReplayController {

    private final DltReplayService dltReplayService;

    @PostMapping("/replay")
    public Map<String, Object> replay(@RequestParam(defaultValue = "50") int max) {

        if (max <= 0 || max > 500) {
            // Guardrail: prevents someone replaying 1 million msgs by mistake
            return Map.of(
                    "status", "FAILED",
                    "reason", "max must be between 1 and 500"
            );
        }

        DltReplayService.ReplayResult result = dltReplayService.replayFromDlt(max);

        return Map.of(
                "status", "OK",
                "dltMessagesPolled", result.polled(),
                "messagesReplayedToMainTopic", result.replayed()
        );
    }
}