# PulseFit — Booking Service

## Project Description

Owns class bookings for PulseFit. Backed by MongoDB (the non-relational
database requirement). Before saving a booking it calls `member-service`
and `class-service` directly — resolved through Eureka via a
`@LoadBalanced` `RestClient` — to confirm both the member and the class
exist, and snapshots the member's name and the class's schedule onto the
booking document. Every booking create/cancel also writes a lightweight
event record to Firestore (`FirestoreAuditService`), demonstrating the
module's Firestore requirement.

## Technology Stack

- Java 25
- Spring Boot 4.0.8 (Spring Web MVC)
- Spring Data MongoDB
- Spring Cloud Eureka Client + Config Client + LoadBalancer (`@LoadBalanced RestClient`)
- Google Cloud Firestore client (`google-cloud-firestore`)
- PM2 (process management on the deployed VM)

## API

| Method | Path                          | Description                              |
|--------|-------------------------------|-------------------------------------------|
| POST   | `/api/bookings`                | Create a booking (validates member + class via their services) |
| GET    | `/api/bookings`                | List all bookings                         |
| GET    | `/api/bookings/{id}`           | Get one booking                           |
| GET    | `/api/bookings/member/{memberId}` | List a member's bookings                |
| PUT    | `/api/bookings/{id}/cancel`    | Cancel a booking                          |
| DELETE | `/api/bookings/{id}`           | Delete a booking                          |

Example create request body:

```json
{
  "memberId": 1,
  "classId": 3
}
```

A failed downstream call (member-service or class-service unreachable, or
the given id doesn't exist there) returns `502 Bad Gateway` with a message
naming which service failed — not a generic 500 — so it is easy to tell
apart from a bug in booking-service itself.

## Setup / Getting Started

### Prerequisites

- Java 25 JDK, Maven 3.9+
- A MongoDB instance reachable locally (`mongodb://localhost:27017` by default)
- `member-service`, `class-service`, `service-registry` and `config-server`
  running for the full create-booking flow to work (booking-service will
  still start without them, but `POST /api/bookings` will fail until they're
  reachable)

### Run locally

```bash
mvn clean package
java -jar target/booking-service.jar
```

## Student Information

- **Student Name:** Pasan Nimila
- **Student Number:** 2301692034
- **Slack Handle:** pasan_nimila (optional)
- **GCP Project ID:** pulsefit-capstone
