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

## 🧩 Core Modules

### 🛒 `order-service`

* **Description**: Accepts order creation requests via REST APIs. Persists the order to its database and publishes an order event to Kafka using the **Outbox Pattern**.
* **Why?**: Serves as the *source of truth* for all orders, decoupling order creation from subsequent processing and ensuring data consistency with asynchronous event publishing.

---

### 📦 `inventory-service`

* **Description**: Listens to order events from Kafka. Validates product availability and updates stock records. Publishes inventory update events.
* **Why?**: Enables isolated inventory logic, scales independently, and maintains separation of concerns between order and inventory management.

---

### 💰 `payment-service`

* **Description**: Consumes order events from Kafka to process payments. Publishes payment status events (e.g., success/failure).
* **Why?**: Handles payment processing in a separate, dedicated service, ensuring secure and scalable financial operations.

---

### 📊 `order-aggregator`

* **Description**: Utilizes **Kafka Streams** to join and aggregate various order-related events (e.g., order created, inventory reserved, payment processed) to build and maintain a real-time, materialized view of current order states.
* **Why?**: Provides real-time analytics and a consolidated view of order statuses without burdening core transactional services.

---

### 📈 `dashboard-service`

* **Description**: Queries the materialized views created by `order-aggregator` or data streamed to OpenSearch/Elasticsearch to present real-time order statuses and analytical insights via a user interface.
* **Why?**: Enables real-time monitoring and reporting for business users and operational teams.

---

### 🪵 `log-shipper`

* **Description**: (Conceptual/Utility) Responsible for shipping service logs to a centralized logging system like **OpenSearch** (part of the **ELK** stack).
* **Why?**: Centralized logging is crucial for observability, debugging, and operational monitoring in a distributed microservices environment.

---

### 📄 `schema-registry`

* **Description**: Manages and stores **Avro** schemas for all Kafka events, ensuring schema evolution compatibility. Run via **Confluent** or **Apicurio**.
* **Why?**: Critical for data governance, ensuring that producers and consumers understand the message format and allowing for graceful schema changes over time.

---

### 🔗 `kafka-connect`

* **Description**: A sink connector configured to stream data from Kafka topics (e.g., aggregated order data) to **OpenSearch** or **Elasticsearch**.
* **Why?**: Facilitates efficient data indexing for search, analytics, and reporting dashboards.

---

### 📚 `common-library`

* **Description**: A shared module containing common models, **Avro** schemas, utilities, and configurations used across multiple services to maintain consistency and reduce code duplication.
* **Why?**: Promotes reusability and ensures a single source of truth for shared data structures and utilities.

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

## 📂 Repository Structure

Enterprise-grade real-time analytics system using:
- Spring Boot Microservices
- Apache Kafka (Streams, Connect, Schema Registry, Transactions)
- Docker, Jenkins, GitHub Actions, ELK, Prometheus/Grafana
- AI for log summarization and real-time issue detection
- TDD with full JUnit coverage

---

## 🤝 Contribution Guidelines

- Each module should be a Gradle subproject under this umbrella repo.
- Ensure unit tests are written using TDD.
- Keep Docker setups modular for Kafka, Zookeeper, and services.
- Use meaningful commit messages and modular PRs.

