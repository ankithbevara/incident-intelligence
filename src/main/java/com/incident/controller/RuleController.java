package com.incident.controller;

import com.incident.dto.CreateRuleRequest;
import com.incident.entity.Rule;
import com.incident.service.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rule create(@Valid @RequestBody CreateRuleRequest req) {
        return ruleService.createRule(req);
    }

    @GetMapping
    public List<Rule> list() {
        return ruleService.listRules();
    }
}
