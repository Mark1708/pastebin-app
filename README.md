# pastebin-app

> Full-stack pastebin demo with a Spring Boot API, React UI, Keycloak login, PostgreSQL metadata storage, MinIO paste-content storage, and Hazelcast-backed caching.

![Backend](https://img.shields.io/badge/backend-spring%20boot%203.0.10-111827?style=for-the-badge&labelColor=111827&color=5b5ef4)
![Frontend](https://img.shields.io/badge/frontend-react%2018-111827?style=for-the-badge&labelColor=111827&color=5b5ef4)
![Auth](https://img.shields.io/badge/auth-keycloak%2020-111827?style=for-the-badge&labelColor=111827&color=5b5ef4)

[Русская версия](README.ru.md)

| Field | Value |
|---|---|
| Status | Finished sandbox demo |
| Type | Full-stack demo / integration playground |
| Primary stack | Java 17, Spring Boot 3.0.10, React 18, CRA 5, Keycloak 20, PostgreSQL 14, MinIO, Hazelcast |
| Main flow | Authenticated user creates a paste, backend stores metadata in PostgreSQL and content in MinIO, then retrieves by hash |

## Project scope

- Demo / sandbox project, not a production deployment.
- Useful for local experiments with JWT/OIDC authentication, object storage, API documentation, caching, and containerized dependencies.
- Intended to show architecture and integration points rather than a hardened deployment model.

## Quick start

```shell
cp .env.example .env
# Fill placeholder values before first run.
docker compose up -d
```

Run the backend and frontend in separate terminals:

```shell
cd pastebin-backend
./gradlew bootRun --args='--spring.profiles.active=dev'
```

```shell
cd pastebin-frontend
npm install
npm start
```

## Service matrix

| Service | Path / image | Runtime/framework | Port | Responsibility | Verify command |
|---|---|---|---:|---|---|
| Frontend | `pastebin-frontend/` | React 18.2.0, CRA 5.0.1, Keycloak JS 20.0.3, PrimeReact 9.3.0 | 3000 | Browser UI, Keycloak login, paste create/find forms | `npm test`, `npm run build` |
| Backend | `pastebin-backend/` | Java 17, Spring Boot 3.0.10, Gradle 8.2.1 | 8081 | REST API, JWT validation, persistence orchestration | `./gradlew test`, `./gradlew build` |
| PostgreSQL | `postgres:14.8-alpine3.18` | Docker Compose | 54322 -> 5432 | Paste metadata and tags | `docker compose ps postgres` |
| Keycloak DB | `postgres:14.8-alpine3.18` | Docker Compose | 5434 -> 5432 | Keycloak persistence | `docker compose ps keycloak-postgres` |
| Keycloak | `quay.io/keycloak/keycloak:20.0.2` | Docker Compose | 8282 -> 8080 | Local OIDC provider, realm `pastebin` | `docker compose ps keycloak` |
| MinIO | `minio/minio` | Docker Compose | 9000, 9001 | Paste content object storage | `docker compose ps minio` |
| Hazelcast Management Center | `hazelcast/management-center:5.2.0` | Docker Compose | 8080 | Local cache observability UI | `docker compose ps hazelcast-management-center` |

## Architecture

```text
React UI (localhost:3000)
  -> Keycloak (localhost:8282, realm pastebin, frontend-client)
  -> Spring Boot API (localhost:8081, Bearer JWT)
       -> PostgreSQL: paste metadata, tags, expiration
       -> MinIO: paste text content under bin/<hash>
       -> Hazelcast: pasteByHash cache and hashQueue
```

Important boundaries:

- Directional UI/API calls are hardcoded for local development: frontend calls `http://localhost:8081`, Keycloak runs at `http://localhost:8282`.
- Backend allows unauthenticated access only to `/api/v1/docs/**`; all other requests require a Bearer JWT.
- Paste text is stored in MinIO. PostgreSQL stores metadata and `content_path`.
- `realm-export.json` is present for Keycloak realm bootstrap. `docker-compose.yml` starts Keycloak with `--import-realm`; if the realm is not imported automatically in a local setup, import that file manually in Keycloak.

## API overview

| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/api/v1/paste` | Bearer JWT | Create a paste and return its hash |
| `GET` | `/api/v1/paste/{hash}` | Bearer JWT | Load paste metadata and content by hash |
| `GET` | `/api/v1/docs/swagger-ui.html` | Public | Swagger UI |
| `GET` | `/api/v1/docs/api-docs` | Public | OpenAPI JSON |

Create payload shape:

```json
{
  "title": "Example",
  "author": "Mark",
  "tags": ["demo", "pastebin"],
  "text": "Paste content",
  "expiration": "N"
}
```

The UI exposes `N`, `10m`, and `1M`. The backend also accepts `1Y`, `1H`, `1D`, `1W`, `2W`, `6M`, or a `yyyy-MM-dd` date.

## Local development

### Backend

```shell
cd pastebin-backend
./gradlew test
./gradlew spotlessCheck
./gradlew spotbugsMain
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Frontend

```shell
cd pastebin-frontend
npm install
npm test
npm run build
npm start
```

## Useful URLs

| Component | URL | Notes |
|---|---|---|
| Frontend | `http://localhost:3000` | React development server |
| Backend API | `http://localhost:8081/api/v1/paste` | Requires Bearer JWT |
| Swagger UI | `http://localhost:8081/api/v1/docs/swagger-ui.html` | Public docs path from `application-dev.yml` |
| OpenAPI JSON | `http://localhost:8081/api/v1/docs/api-docs` | Public docs path from `application-dev.yml` |
| Keycloak | `http://localhost:8282` | Local admin/realm UI |
| Hazelcast Management Center | `http://localhost:8080` | Local observability UI |
| MinIO Console | `http://localhost:9001` | Local object storage console |

## Limitations / security

- Do not use placeholder credentials outside local development.
- Keep `.env` local and replace every placeholder before the first run.
- Local frontend config hardcodes Keycloak and backend URLs; external deployments need configuration work.
- Backend disables CSRF and uses stateless JWT validation for the demo API.
- This repo is intentionally a demo and should not be treated as a secure production deployment.
- No CI is currently configured.

## Status

Portfolio/demo project. The code is public as an architecture and implementation reference, not as a production-ready service.
