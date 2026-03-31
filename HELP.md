# Tracking API — Guide de démarrage

API de suivi GPS en temps réel — Spring Boot 4.0.5 / Java 21 / Architecture Hexagonale.

---

## Prérequis

- Java 21+
- Maven 3.9+
- PostgreSQL (port 5434)
- Redis (port 6379)

---

## Lancer le projet

```bash
./mvnw spring-boot:run
```

L'application démarre sur le port **8083**.

---

## Configuration (`application.properties`)

| Propriété | Valeur par défaut |
|---|---|
| `server.port` | `8083` |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5434/tracking_db` |
| `spring.datasource.username` | `postgres` |
| `spring.datasource.password` | `postgres` |
| `spring.data.redis.host` | `localhost` |
| `spring.data.redis.port` | `6379` |
| `app.tracking.base-url` | `https://app.com` |

---

## Swagger / OpenAPI

| URL | Description |
|---|---|
| `http://localhost:8083/swagger-ui.html` | Interface Swagger UI |
| `http://localhost:8083/v3/api-docs` | Spec OpenAPI JSON |

---

## API REST

### Créer un trajet
```
POST /api/trajets
Content-Type: application/json

{ "clientId": "client-123" }
```
Retourne un `TrajetDto` avec le `trackingUrl` de partage.

### Consulter un trajet (accès public)
```
GET /api/trajets/track/{shareToken}
```

### Historique des positions
```
GET /api/trajets/{trajetId}/history
```
Retourne la liste des positions triées par `timestamp ASC`.

---

## WebSocket (STOMP)

### Connexion
```
ws://localhost:8083/ws
```
Compatible SockJS.

### Envoyer une position (client GPS)
- Destination : `/app/position`
- Payload :
```json
{
  "trajetId": "uuid-du-trajet",
  "latitude": 14.7167,
  "longitude": -17.4677
}
```

### Recevoir les positions en temps réel (viewer)
- S'abonner à : `/topic/trajet/{trajetId}`
- Message reçu :
```json
{
  "id": "uuid",
  "trajetId": "uuid-du-trajet",
  "latitude": 14.7167,
  "longitude": -17.4677,
  "timestamp": "2026-03-31T10:00:00Z"
}
```

---

## Architecture

```
domain/           ← Pur Java, aucune dépendance framework
  model/          ← Trajet, PositionEvent, TrajetStatut
  port/in/        ← Use cases (interfaces)
  port/out/       ← Ports de sortie (interfaces)

application/
  serviceImpl/    ← TrajetServiceImpl (logique métier)

infrastructure/
  adapter/in/
    web/          ← TrajetController (REST), TrajetWebSocketController (STOMP)
    dto/          ← TrajetDto, PositionEventDto
    request/      ← CreateTrajetRequest, SendPositionRequest
  adapter/out/
    persistence/  ← Entities JPA, Repositories, Mappers, Adapters
    websocket/    ← PositionPublisherAdapter (SimpMessagingTemplate)
  config/         ← WebSocketConfig, MdcFilter, OpenApiConfig
```

---

## Logs (ECS)

Chaque requête reçoit un `traceId` automatique via MDC.
Format des logs : `event.action`, `event.outcome` (SUCCESS / FAILURE), contexte métier.

```
2026-03-31 10:00:00 [abc-123] INFO  TrajetServiceImpl - event.action=CREATE_TRAJET, event.outcome=SUCCESS, trajetId=..., shareToken=...
```

---

## Build & Tests

```bash
# Compiler
./mvnw clean package

# Tests
./mvnw test

# Image OCI
./mvnw spring-boot:build-image
```