package com.incident.repository;

import com.incident.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    //database operations
    @Query("""
            SELECT COUNT(e)
            FROM Event e
            WHERE e.serviceName = :serviceName
            AND e.eventType = :eventType
            AND e.occurredAt >= :since
        """) // JPQL query to count events based on serviceName, eventType and time window
        long countRecentEvents(@Param("serviceName") String serviceName,
                               @Param("eventType") String eventType,
                               @Param("since") java.time.Instant since); //method to count recent events
}