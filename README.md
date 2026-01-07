# Real-Time Incident Intelligence Platform

A backend service that ingests operational events via REST APIs, streams them through Kafka, and persists them to PostgreSQL for further analysis and alerting workflows.

This project demonstrates real-world patterns used in incident monitoring, observability, and event-driven backend systems.

## Project status

- Active development
- Core event ingestion pipeline (API → Kafka → Consumer → DB) is implemented and stable

## Key use cases

- Centralized ingestion of application and system events
- Foundation for real-time anomaly detection (error spikes, latency issues)
- Event enrichment and correlation
- Operational visibility for SRE, DevOps, and Platform teams

## Technology stack

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate ORM
- PostgreSQL (event persistence using JSONB)
- Apache Kafka
- Redis (planned: counters / rate-limiting)
- Docker & Docker Compose

## Architecture overview

Client / Service → REST API → Kafka Topic → Kafka Consumer → PostgreSQL

Flow

- Events are received via REST API
- Events are published asynchronously to Kafka
- Kafka consumers process and persist events
- Stored data becomes the basis for alerting and dashboards

## API contract

POST /api/events

Request example

```json
{
	"serviceName": "payment-service",
	"eventType": "ERROR",
	"severity": "HIGH",
	"message": "Timeout while charging card",
	"metadata": {
		"latencyMs": 2300
	}
}
```

Response example

```json
{ "status": "QUEUED" }
```

HTTP Status: 202 Accepted — the event is queued and processed asynchronously.

Quick curl example

```bash
curl -X POST http://localhost:8080/api/events \
	-H "Content-Type: application/json" \
	-d '{"serviceName":"payment-service","eventType":"ERROR","severity":"HIGH","message":"Timeout while charging card","metadata":{"latencyMs":2300}}'
```

## Running the application locally

Prerequisites

- Java 17
- Docker & Docker Compose

Step 1 — Set database credentials

This application does not ship with hardcoded database credentials. Before running the service, set your PostgreSQL credentials as environment variables in your terminal session:

```bash
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
```

Step 2 — Start PostgreSQL + Kafka

From the project root (where `docker-compose.yml` exists):

```bash
docker compose up -d
```

This starts:

- PostgreSQL on localhost:5432
- Kafka on localhost:9092
- Zookeeper (used by Kafka)

Step 3 — Run the Spring Boot service

```bash
./mvnw spring-boot:run
```

Application starts on: http://localhost:8080

Step 4 — Test the endpoint

Send a POST (see curl example above) and expect a 202 Accepted response. Check application logs to see Kafka producer/consumer activity and verify data persisted to PostgreSQL.

## Data model

Events are stored in PostgreSQL with flexible JSON metadata:

- Structured columns for filtering (serviceName, eventType, severity)
- JSONB column for extensible metadata (latency, traceId, region, etc.)

Schema is managed automatically by Hibernate during development.

## Project structure

controller/   → REST endpoints
dto/          → API request models
kafka/        → Producer, consumer, topic definitions
entity/       → JPA entities
repository/   → Database access layer
service/      → Business logic
util/         → Utilities (e.g., JSON mapping)

## Design highlights

- Asynchronous ingestion using Kafka
- Clear separation of concerns (API / messaging / persistence)
- Consumer offset management enables message replay and recovery
- Designed for extensibility (alerting, dashboards, analytics)

## Planned enhancements

- Rule-based alert engine (e.g., “5 errors in 30s”)
- Dead-letter topic (DLQ) and retry strategy
- Alert deduplication and severity scoring
- WebSocket-based live dashboard
- Distributed tracing (OpenTelemetry)
- Alert routing (Slack, Email, Webhooks)

## Author

Ankith Bevara