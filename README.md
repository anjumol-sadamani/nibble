# Nibble - Order Management System

A Spring Boot microservices project we're building to learn event-driven architecture and distributed systems patterns. This is an early-stage project where we're implementing core patterns step by step.
##  Architecture

This is a monorepo with multiple microservices that communicate through Kafka events. We're exploring how to handle failures and maintain consistency in distributed systems.

**Services:**
- **Order Service** - Order processing with Kafka events and circuit breaker
- **User Service** - User management with OAuth2 authentication
- **Vendor Service** - Vendor management
- **Payment Service** - Payment processing

**Key Patterns:**
- Event-driven communication via Kafka
- Circuit breaker for external service calls
- OAuth2 + JWT authentication
- Shared auth library to avoid code duplication

## Quick Start

1. **Start infrastructure**
   ```bash
   docker-compose up -d
   ```

2. **Run services**
   ```bash
   ./gradlew :services:order-service:bootRun
   ./gradlew :services:user-service:bootRun
   ```

##  Tech Stack

- Java 21, Spring Boot 3.2
- PostgreSQL, Kafka
- Resilience4j, OAuth2

##  API Example

**Create Order:**
```bash
POST localhost:8080/api/v1/order
```
```json
{
  "vendorId": 1,
  "customerId": 1,
  "items": [
    {
      "itemName": "Pizza",
      "price": 12.99,
      "quantity": 2
    }
  ]
}
```

## Events

**OrderCreated Event** published to `order-created` topic:
```json
{
  "orderId": 123,
  "customerId": 456,
  "totalPrice": 25.98,
  "status": "PENDING",
  "orderItems": ["..."]
}
```

##  Resilience

**Circuit Breaker** on vendor service calls:
- Opens after 50% failure rate
- 10-second recovery window
- Graceful fallback behavior

##  Next Steps

- **Complete Basic Flow** - Add vendor and payment service implementations
- **Outbox Pattern** - Guaranteed event delivery for reliable distributed transactions
- **Observability** - Distributed tracing, metrics, and centralized logging
- **API Gateway** - Centralized routing and authentication

## 📋 Services

```
├── order-service/     # Core order processing
├── user-service/      # OAuth2 authentication  
├── vendor-service/    # Vendor management
├── payment-service/   # Payment processing
└── auth-common/       # Shared JWT utilities
```