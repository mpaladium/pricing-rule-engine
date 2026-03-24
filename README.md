# Pricing Rule Engine

Pricing Rule Engine API server built with Java and Spring Boot. A project demonstrating functional pricing engine. The setup is a simple setup yet powerful enough to scale for production

## Prerequisites

- Java 21
- Docker (optional, for container build/run)
- Maven 3.9+ (or use the included Maven Wrapper)

## Project Setup

1. Clone the repository.
2. Move into the project directory.
3. Build the project:

```bash
./mvnw clean install
```

## Configuration

- Default config is in `src/main/resources/application.yml`.
- Environment variable template is provided in `.env.example`.
- Important env vars:
  - `SERVER_PORT` (default `8080`)
  - `API_VERSION` (default `v1`)
  - `LOG_LEVEL_ROOT`, `LOG_LEVEL_APP`
  - `MANAGEMENT_ENDPOINTS`

## Pricing Design

- Deterministic orchestration pipeline:
  - compute base price
  - apply discount rules
  - apply voucher
  - apply shipping
- Extension points are interface-based, so new pricing rules, calculation models, and voucher/discount types can be added without rewriting the core flow.
- All price changes are adjustments: `BASE`, `DISCOUNT`, `SHIPPING`.
- Response includes explainable breakdown, applied rules, and decision trail.

## Run the App (Local)

```bash
./scripts/run.sh
```

or

```bash
./mvnw spring-boot:run
```

## API Endpoints (v1)

- Health check: `GET /v1/health`
  - Example: `http://localhost:8080/v1/health`

## Swagger / OpenAPI

- Swagger UI: `http://localhost:8080/swagger`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Operational Endpoints

- Actuator health: `http://localhost:8080/actuator/health`
- Readiness probe: `http://localhost:8080/actuator/health/readiness`
- Liveness probe: `http://localhost:8080/actuator/health/liveness`
- Prometheus metrics: `http://localhost:8080/actuator/prometheus`

## Run Tests (Local)

```bash
./scripts/test.sh
```

or

```bash
./mvnw test
```

## Docker Build and Run

```bash
docker build -t pricing-rule-engine:latest .
docker run --rm -p 8080:8080 --env-file .env.example pricing-rule-engine:latest
```
