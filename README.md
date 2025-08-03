# Product Order Service

[![Build](https://github.com/tzzlabs/product-order-service/actions/workflows/maven.yml/badge.svg)](https://github.com/tzzlabs/product-order-service/actions/workflows/maven.yml)

Spring Boot microservice for managing product orders with role-based authentication, PostgreSQL persistence, and Swagger UI.

## Tech Stack
- Java 17, Spring Boot 3.5
- Spring Security (HTTP Basic with in-memory users)
- PostgreSQL
- Springdoc OpenAPI (Swagger UI)
- Docker (planned)
- CI/CD with GitHub Actions (basic build)
- Deployment target: AWS EC2 (planned)

## Quickstart (local)
1. Copy template:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties