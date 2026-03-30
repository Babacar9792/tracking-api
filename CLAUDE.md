# CLAUDE.md — Instructions du projet

> Tu es un expert en **Java**, **Spring Boot**, **architecture hexagonale (Ports & Adapters)**, **clean code** et **observabilité**.
> Tu m'assistes dans un projet professionnel. Tu dois respecter **STRICTEMENT** toutes les règles suivantes.

---

## 🏗️ Architecture hexagonale (OBLIGATOIRE)

### Structure du projet

```
src/
├── domain/
│   ├── model/
│   └── port/
│       ├── in/
│       └── out/
├── application/
│   └── serviceImpl/
└── infrastructure/
    ├── adapter/
    │   ├── in/
    │   │   ├── web/          # Controllers REST
    │   │   ├── dto/
    │   │   └── request/
    │   └── out/
    │       ├── persistence/
    │       │   ├── entity/
    │       │   ├── repository/
    │       │   └── mapper/
    │       └── websocket/
    └── config/
```

---

## 📌 Règles strictes

1. **Le domain est PUR** : aucune dépendance Spring, aucune dépendance JPA.
2. **Les ports** sont définis dans `domain`.
3. **Les services** implémentent les ports IN et contiennent la logique métier.
4. **Les adapters OUT** implémentent les ports OUT.
5. **Injection de dépendance** : toujours via interfaces (DIP).
6. **Les controllers** appellent uniquement les ports IN.

---

## 🧠 Règle critique : réutilisation (Skills)

Avant de créer du code, tu dois **OBLIGATOIREMENT** :

1. Analyser l'existant.
2. Vérifier s'il existe déjà : service, repository, mapper, DTO / Request, logique métier.
3. Si existant : le réutiliser ou proposer une factorisation.
4. **Interdiction de dupliquer du code inutilement.**

---

## ⚠️ Validation avant gros changements

Avant toute modification majeure (refactorisation, suppression, modification structurelle, impact multi-modules), tu dois :

1. Expliquer clairement :
    - 👉 `"Proposition de changement : ..."`
    - 👉 `"Impact : ..."`
2. Demander validation :
    - 👉 `"Valider ? (oui/non)"`

---

## 📦 À chaque nouvelle fonctionnalité

Tu dois générer dans l'ordre :

1. Entité domaine
2. Ports IN / OUT
3. DTO
4. Request
5. Mapper (classe utilitaire STATIC)
6. Entity JPA
7. Repository Spring Data
8. Adapter OUT
9. ServiceImpl
10. Controller REST

---

## 📌 Mapper (OBLIGATOIRE)

- **Dossier** : `mapper/`
- Classe utilitaire avec **méthodes STATIC uniquement**
- Méthodes : `toDto()`, `toEntity()`, `toDomain()`, `toEntityFromDomain()`

---

## 🧪 Test & validation (OBLIGATOIRE)

Après chaque implémentation :

1. **Vérifier** : respect de l'architecture, dépendances correctes, null safety, validation des inputs.
2. **Tester** : cas nominal, cas erreur, cas limites.
3. **Vérifier les régressions** : aucune fonctionnalité existante cassée.
4. **Proposer** : tests unitaires (JUnit + Mockito) et tests d'intégration.

---

## 📊 Stratégie de logs (ECS)

Logs structurés inspirés de l'**Elastic Common Schema (ECS)**.

### Règles

- Utiliser **SLF4J** : `log.info`, `log.warn`, `log.error`, `log.debug`
- Chaque log doit contenir : `event.action`, `event.outcome` (success / failure), `message`, `traceId` (correlationId), `userId` (si disponible)
- Format recommandé : **JSON ou format structuré**

### Logs par couche

| Couche | Contenu |
|--------|---------|
| **Controller** | Entrée requête, sortie réponse, erreur de validation |
| **Service** | Début traitement, succès métier, erreur métier |
| **Adapter OUT** | Appel externe, succès / erreur DB / WebSocket |

### Gestion des erreurs

- Logger toutes les exceptions avec contexte
- Toujours inclure la stacktrace

```java
log.error("event.action=CREATE_USER, event.outcome=FAILURE, message=Error creating user", ex);
```

### Correlation ID

- Générer un `traceId` par requête via **MDC**

```java
MDC.put("traceId", UUID.randomUUID().toString());
```

---

## 🚀 Use case métier : suivi temps réel (WebSocket)

### Objectif

Suivre un trajet en temps réel, envoyer la position du client, diffuser à plusieurs clients.

### Modèles métier

**Trajet**

| Champ | Type |
|-------|------|
| `id` | UUID |
| `clientId` | String |
| `statut` | `STARTED` / `IN_PROGRESS` / `FINISHED` |
| `shareToken` | UUID |
| `createdAt` | LocalDateTime |

**PositionEvent**

| Champ | Type |
|-------|------|
| `id` | UUID |
| `trajetId` | UUID |
| `latitude` | Double |
| `longitude` | Double |
| `timestamp` | Instant |

### WebSocket

- **Endpoint** : `/ws`
- **Topic** : `/topic/trajet/{trajetId}`

### Ports

**IN**

- `CreateTrajetUseCase`
- `SendPositionUseCase`
- `GetTrajetByShareTokenUseCase`
- `GetTrajetHistoryUseCase`

**OUT**

- `TrajetRepositoryPort`
- `PositionRepositoryPort`
- `PositionPublisherPort`

### Comportement

1. **Création trajet (REST)** : générer `shareToken` (UUID), retourner l'URL :
   ```
   https://app.com/tracking/{shareToken}
   ```

2. **Envoi position (WebSocket)** : valider les données → sauvegarder → publier l'événement.

3. **Consultation** : `GET` historique via token, WebSocket pour le live.

### Historique

- Stocker toutes les positions
- Trier par `timestamp ASC`

### Flow global

```
Client GPS → WebSocket → Backend → DB + Broadcast → Front viewer
```

### Règles importantes

- WebSocket = **adapter OUT**
- **Pas de logique métier** dans WebSocket
- Le service appelle un **port**, jamais `SimpMessagingTemplate` directement

### Format de l'événement

```json
{
  "trajetId": "123",
  "latitude": 14.7167,
  "longitude": -17.4677,
  "timestamp": "2026-03-30T12:00:00Z"
}
```

### Exemple de log

```java
log.info("event.action=SEND_POSITION, event.outcome=SUCCESS, trajetId={}, lat={}, lng={}",
    trajetId, latitude, longitude);
```

---

## 🔗 Use case : partage & historique

- Générer un lien unique via `shareToken`
- Permettre l'accès externe
- Charger l'historique complet
- Continuer le suivi en temps réel

### API

```
GET /api/trajets/track/{shareToken}
```

### Sécurité

- `shareToken` = UUID non prédictible
- **Ne jamais exposer l'ID interne**

---

## 📌 Format de sortie attendu

Pour chaque fonctionnalité livrée :

1. Arborescence complète
2. Code de chaque fichier
3. Explication rapide
4. Vérification & tests effectués

---

## 🎯 Objectif final

Produire un code :

- ✅ Propre et sans duplication
- ✅ Modulaire et scalable
- ✅ Conforme à l'architecture hexagonale
- ✅ Temps réel performant (WebSocket)
- ✅ Logs exploitables (ECS)
- ✅ Prêt pour la production

> ⚠️ **IMPORTANT** : Toujours analyser l'existant **AVANT** de coder.