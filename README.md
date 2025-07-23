# Smart Order Analytics & Reporting (SOAR)

## 🧠 Project Overview

**SOAR** (Smart Order Analytics & Reporting) is an enterprise-level microservices-based application designed to simulate a real-world **e-commerce order processing** and **analytics pipeline** using **Apache Kafka** for asynchronous communication and **Spring Boot** for scalable backend services.

The project is built to help master real-time data streaming, Kafka Streams processing, observability, and microservice best practices in a production-grade setup.

---

## 🚀 Core Objectives

- Process orders in real-time and asynchronously.
- Analyze and aggregate order data using Kafka Streams.
- Demonstrate event-driven microservice architecture with Spring Boot & Kafka.
- Showcase enterprise features like observability, fault tolerance, and modular scaling.
- Serve as an interview-ready Kafka + Microservices project.

---

## 🏗️ Architecture Diagram




---

## 🧩 Module Responsibilities

### ✅ `order-service`

- Accepts order creation requests via REST APIs.
- Produces Kafka messages to `order-events` topic.
- Applies validations and business rules.
- Uses `KafkaTemplate` to publish events.

**Why?**  
This service is the *source of truth* for all orders. It decouples order creation from processing, enabling async scaling.

---

### 📦 `inventory-service`

- Listens to `order-events` from Kafka.
- Validates product availability.
- Updates stock records and produces `inventory-events`.

**Why?**  
Enables isolated inventory logic, scales independently, and keeps order and inventory concerns separate.

---

### 📊 `analytics-service` (planned)

- Uses **Kafka Streams** to aggregate events.
- Provides materialized views and reports (e.g., top-selling items).
- Pushes results to a queryable store or OpenSearch.

**Why?**  
Handles real-time analytics without burdening core services. Kafka Streams gives performance and fault tolerance.

---

## ⚙️ Kafka Configuration Strategy

- Kafka configs like `bootstrap.servers` can be placed in `application.yml`.
- Custom `KafkaConfig.java` allows programmatic creation of:
    - `ProducerFactory`
    - `KafkaTemplate`
    - Consumer/Producer serializers
- This provides flexibility when building complex topologies or supporting multiple clusters.

**Why KafkaTemplate?**  
Spring's `KafkaTemplate` simplifies publishing events and handles serialization, retry, error handling behind the scenes.

---

## 🔧 Tech Stack

| Layer              | Technology                  |
|-------------------|-----------------------------|
| Language           | Java 17                     |
| Framework          | Spring Boot 3.x             |
| Messaging          | Apache Kafka                |
| Serialization      | JSON (later: Avro + Schema Registry) |
| Build Tool         | Gradle                      |
| Containerization   | Docker                      |
| Observability (upcoming) | OpenSearch / ELK / Prometheus |

---

## 📈 Features Planned (Post-MVP)

- Kafka Streams for aggregation and projections.
- Schema Registry for schema evolution.
- Outbox Pattern + CQRS using Kafka.
- Error handling with Retry + Dead Letter Topics.
- Kafka Connect + Debezium for syncing DB → Kafka.
- Centralized Logging (ELK) & Monitoring (Prometheus + Grafana).
- AI-assisted log summarization using Spring AI.
- Circuit Breaker, Service Discovery, API Gateway.
- Secure JWT-based auth with role-based access.

---

## 🤝 Contribution Guidelines

- Each module should be a Gradle subproject under this umbrella repo.
- Ensure unit tests are written using TDD.
- Keep Docker setups modular for Kafka, Zookeeper, and services.
- Use meaningful commit messages and modular PRs.

---

## 📂 Repository Structure



Enterprise-grade real-time analytics system using:
- Spring Boot Microservices
- Apache Kafka (Streams, Connect, Schema Registry, Transactions)
- Docker, Jenkins, GitHub Actions, ELK, Prometheus/Grafana
- AI for log summarization and real-time issue detection
- TDD with full JUnit coverage