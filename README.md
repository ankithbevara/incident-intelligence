# Real-Time Incident Intelligence Platform

## Overview:

    1. This project is a backend service designed to ingest, process, and analyze system events in real time. It enables engineering and operations teams to detect failures, anomalies, and abnormal behavior across distributed systems before they escalate into incidents.
    2. The system focuses on reliability, scalability, and extensibility, following patterns commonly used in production-grade monitoring and alerting platforms.

## Project Status:

    This project is actively evolving, with ongoing improvements and feature additions.

### Key Use Cases:

    ~ Centralized ingestion of application and system events
    ~ Real-time detection of abnormal behavior (error spikes, latency issues)
    ~ Event correlation and enrichment
    ~ Alert generation for operational teams
    ~ Foundation for dashboards and alerting pipelines
    ~ This type of system is commonly used by **SRE**, **DevOps**, and **Platform teams**.

## Technology Stack

### Backend

    - Java 17
    - Spring Boot
    - Spring Data JPA
    - Hibernate ORM

### Data & Messaging

    - PostgreSQL (event persistence)
    - Apache Kafka (event streaming)
    - Redis (rate limiting / counters)

### Infrastructure

    - Docker & Docker Compose
    - REST APIs
    - JSON-based messaging

## Architecture Overview

Client / Service
     |
     v
[ REST API ]
     |
     v
[ Event Ingestion Layer ]
     |
     v
[ Kafka Topic ]
     |
     v
[ Event Processor ]
     |
     v
[ PostgreSQL + Alerts ]

    1. Events are ingested through REST APIs.
    2. Data is validated and published to Kafka.
    3. Consumers process, enrich, and persist events.
    4. Downstream systems (alerts, dashboards) react in near real time.

### Core Features:

    - Event ingestion API for structured system events
    - Kafka-based event pipeline for scalability and decoupling
    - JSON-based event payloads with flexible schemas
    - PostgreSQL storage using JSONB
    - Pluggable alerting logic (thresholds, patterns, severity)
    - Clean layered architecture (Controller → Service → Repository)

## Sample Event Payload

{
"serviceName": "payment-service",
"eventType": "ERROR",
"severity": "HIGH",
"message": "Timeout while charging card",
"metadata": { "latencyMs": 2300 }
}

## Running the Application

    ./mvnw spring-boot:run

## The service starts on:

    http://localhost:8080

### Configuration:

    This application uses environment variables for sensitive configuration.

Required variables:

- `DB_USERNAME`
- `DB_PASSWORD`

Database schema is managed automatically by Hibernate.

## API Endpoint

    POST /api/events
        - Accepts structured event data and stores it for further processing.

## Design Highlights:

    ~ Clean separation of concerns (Controller / Service / Repository)
    ~ JSON-based extensible event model
    ~ Production-oriented error handling
    ~ Ready for Kafka-based async processing
    ~ Built with scalability and observability in mind

### Future Enhancements:

    - Rule-based alert engine
    - WebSocket-based live dashboard
    - Distributed tracing (OpenTelemetry)
    - Alert routing (Slack, Email, PagerDuty)
    - Event replay and analytics

## Author- Ankith Bevara

    Built as a backend-focused system design and implementation project to demonstrate real-world engineering practices in distributed systems and event-driven architectures.
