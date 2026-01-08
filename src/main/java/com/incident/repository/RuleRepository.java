package com.incident.repository;

import com.incident.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RuleRepository extends JpaRepository<Rule, UUID> {
    List<Rule> findByEnabledTrueAndServiceNameAndEventType(String serviceName, String eventType);
}
