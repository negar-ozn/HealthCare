### Healthcare Management System

A microservices-based Healthcare Management System built with Java 25 and Spring Boot, designed to manage healthcare operations through independently structured services, centralized authentication, event-driven communication, and distributed infrastructure.

The project focuses on backend engineering practices, including microservice architecture, secure authentication and authorization, database management, caching strategies, asynchronous communication, and audit logging.

##### Project Status: 🚧 Actively under development

### Table of Contents

- Overview
- Microservices
- Key Features
- Technology Stack
- Architecture and Engineering Practices
- Infrastructure
- Getting Started
- Future Improvements

### Overview

The Healthcare Management System is designed to support essential healthcare operations, including patient management, doctor and department administration, appointment scheduling, personnel management, and invoicing.

The system follows a microservices architecture, separating business responsibilities into independently organized services.

Each service has its own responsibilities and data management, while Apache Kafka enables asynchronous communication between services.

The primary objectives are:

- Building a scalable and maintainable backend architecture.
- Implementing centralized authentication and fine-grained authorization.
- Managing appointment scheduling and booking workflows.
- Supporting asynchronous inter-service communication.
- Improving data retrieval performance through Redis caching.
- Maintaining traceability through centralized audit logging.
- Containerizing application infrastructure with Docker

### Microservices

### 1. Healthcare API

The core business service responsible for healthcare operations.

- Main domains include:
- Patient management
- Doctor management
- Department management
- Appointment management
- Schedule management
- Personnel management
- Invoice management

It also integrates with the authentication service for authorization and uses Redis for caching and identifier generation.

### 2. Healthcare Auth

A dedicated authentication and authorization service.

### Responsibilities:

- User registration and login
- JWT-based authentication
- Token validation and introspection
- Role-based access control (RBAC)
- Permission-based authorization
- Account locking and failed-login tracking
- Password management
- Email verification workflows

The service manages users, roles, and permissions independently of the core healthcare application.

### 3. Healthcare Audit

A centralized audit service responsible for recording application activities.

Audit information includes:

- User ID and username
- Action performed
- Entity name and identifier
- Timestamp
- IP address
- Operation status
- Additional details

The service supports tracking important business and authentication activities.

### 4. Healthcare Notification

A dedicated service responsible for handling notification-related operations.

It participates in asynchronous communication through Apache Kafka, allowing notification processing to remain independent of the core business workflows.

### 5. Common Kafka

A shared module containing reusable Kafka-related components and event contracts.

It provides common event structures, enums, and communication abstractions to maintain consistency between services.

## Key Features

### Authentication and Authorization

- JWT-based authentication.
- Centralized authentication service.
- Remote token introspection.
- Role-based access control (RBAC).
- Fine-grained permission-based authorization.
- Method-level security using Spring Security.
- Account locking and failed-login protection.

### Patient and Healthcare Management

- Patient registration and management.
- Doctor and department administration.
- Personnel management.
- Healthcare resource organization.

### Appointment and Scheduling

- Doctor schedule management.
- Availability and appointment slot management.
- Appointment booking.
- Appointment status management.
- Schedule reservation and cancellation workflows.
- Concurrency considerations for appointment booking.

### Invoice Management

- Invoice-related business operations.
- Event-driven integration for invoice-related workflows.

### Redis Integration

Redis is used within the core healthcare service for:

- Generating unique business identifiers.
- Caching frequently accessed department information.
- Improving read performance.
- Supporting cache invalidation strategies.

### Event-Driven Communication

Apache Kafka enables asynchronous communication between services.

The shared common-kafka module provides reusable event contracts and common Kafka components.

### Audit Logging

Centralized logging of important system activities, including authentication events and business operations.

### Technology Stack

### Category - Technologies

- Programming Language: Java 25
- Backend Framework: Spring Boot
- Security: Spring Security
- ORM: Spring Data JPA / Hibernate
- Database: PostgreSQL 
- Database Migration: Flyway 
- Caching; Redis 
- Messaging: Apache Kafka 
- Containerization: Docker / Docker Compose
- Build Tool: Apache Maven
- Testing: JUnit 5 / Spring Boot Test

### Architecture and Engineering Practices

The project incorporates several backend engineering concepts:

### Microservices Architecture

- Separation of business responsibilities.
- Independent service modules.
- Dedicated persistence for services.
- Shared infrastructure and event contracts.

### Security

- Centralized authentication.
- JWT-based security.
- Remote token introspection.
- Role and permission-based access control.
- Method-level authorization.

### Data Management

- PostgreSQL relational databases.
- JPA entity relationships.
- Optimistic locking using @Version.
- Database schema versioning with Flyway.

### Caching

- Redis-based caching. 
- Cache invalidation after successful database transactions.
- Event-driven cache invalidation. 
- JSON serialization for cached objects.

### Asynchronous Processing

- Apache Kafka integration. 
- Shared event contracts. 
- Event-driven communication between services.

### Reliability and Maintainability

- Separation of concerns. 
- Centralized audit logging.
- Docker-based infrastructure.
- Modular project organization.

### Infrastructure

The development environment uses Docker Compose to manage infrastructure dependencies and application services.

Infrastructure components include:

- PostgreSQL
- Redis
- Apache Kafka

Monitoring infrastructure is also part of the development environment:

- Prometheus
- Grafana

Environment-specific configuration is managed separately from application source code.

### Getting Started

### Prerequisites

Make sure the following tools are installed:

- JDK 25
- Apache Maven
- Docker Desktop
- Git

### 1. Clone the Repository

git clone <repository-url>
cd healthcare-system

### 2. Configure Environment Variables

Create your environment configuration using the provided example:

cp .env.example .env

Update the environment variables according to your local configuration.

Never commit actual credentials, passwords, or secret keys.

### 3. Start Infrastructure

Run Docker Compose:

docker compose up -d

### 4. Build the Project

Build the Maven modules:

mvn clean install

### 5. Run the Services

Start each microservice using its application configuration or the corresponding Docker setup.

Ensure that the required infrastructure services are running and that database connections and service configurations are properly configured.

### Future Improvements

The project is actively evolving. Planned improvements include:

- Strengthening distributed appointment booking consistency.
- Improving Redis cache consistency and concurrency handling. 
- Expanding automated integration and security testing.
- Enhancing service observability and monitoring.
- Improving Docker security and production configurations. 
- Expanding notification workflows. 
- Improving distributed tracing and correlation ID propagation.

### Author

Developed as a backend engineering project to explore and implement modern Java, Spring Boot, and distributed system development practices.

Technologies: Java · Spring Boot · Spring Security · PostgreSQL · Redis · Apache Kafka · Docker

### License

Copyright © 2026 Negar-ozn. All rights reserved.

This project is publicly available for portfolio and educational review purposes only.

See the [LICENSE](LICENSE) file for details.