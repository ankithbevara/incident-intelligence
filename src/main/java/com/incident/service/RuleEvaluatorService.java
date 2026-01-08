// the brain + hook to evaluate rules against incoming events (kafka consumer will call this service after saving event)

package com.incident.service;

import com.incident.entity.Event;
import com.incident.entity.Incident;
import com.incident.entity.Rule;
import com.incident.repository.EventRepository;
import com.incident.repository.IncidentRepository;
import com.incident.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuleEvaluatorService {

    private final RuleRepository ruleRepository;
    private final EventRepository eventRepository;
    private final IncidentRepository incidentRepository;

    public void evaluate(Event savedEvent) {

        // Only evaluate rules that match this service + type, and are enabled
        List<Rule> rules = ruleRepository.findByEnabledTrueAndServiceNameAndEventType(
                savedEvent.getServiceName(),
                savedEvent.getEventType()
        );

        for (Rule rule : rules) {

            // Cooldown check (prevents incident spam)
            if (rule.getLastTriggeredAt() != null) {
                Instant nextAllowed = rule.getLastTriggeredAt().plusSeconds(rule.getCooldownSeconds()); //calculate when the next incident can be triggered based on cooldown
                if (Instant.now().isBefore(nextAllowed)) { //if current time is before the next allowed time, skip this rule
                    continue;
                }
            }

            Instant since = Instant.now().minusSeconds(rule.getWindowSeconds()); //calculate the start time of the evaluation window
            long count = eventRepository.countRecentEvents(rule.getServiceName(), rule.getEventType(), since); //count events matching the rule criteria within the time window

            if (count >= rule.getThreshold()) {
                // Trigger incident
                Incident incident = Incident.builder()
                        .id(UUID.randomUUID())
                        .ruleId(rule.getId())
                        .serviceName(rule.getServiceName())
                        .severity(rule.getSeverity())
                        .status("OPEN")
                        .summary(rule.getThreshold() + " " + rule.getEventType() + " events in " + rule.getWindowSeconds() + "s for " + rule.getServiceName())
                        .triggeredAt(Instant.now())
                        .build();

                incidentRepository.save(incident);

                // Update cooldown marker
                rule.setLastTriggeredAt(Instant.now());
                ruleRepository.save(rule);
            }
        }
    }
}
