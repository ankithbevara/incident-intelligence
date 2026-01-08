package com.incident.service;

import com.incident.dto.CreateRuleRequest;
import com.incident.entity.Rule;
import com.incident.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleRepository ruleRepository;

    public Rule createRule(CreateRuleRequest req) {
        Rule rule = Rule.builder()
                .id(UUID.randomUUID())
                .name(req.getName())
                .serviceName(req.getServiceName())
                .eventType(req.getEventType())
                .threshold(req.getThreshold())
                .windowSeconds(req.getWindowSeconds())
                .severity(req.getSeverity())
                .cooldownSeconds(req.getCooldownSeconds())
                .enabled(req.isEnabled())
                .createdAt(Instant.now())
                .build();

        return ruleRepository.save(rule);
    }

    public List<Rule> listRules() {
        return ruleRepository.findAll();
    }
}
