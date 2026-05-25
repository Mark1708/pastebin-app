# pastebin-app

Full-stack demo of a pastebin-style application with a Spring Boot backend, React frontend, Keycloak authentication, PostgreSQL, MinIO, and Hazelcast Management Center.

[Русская версия](README.ru.md)

## Project scope

- Demo / sandbox project, not a production deployment.
- Useful for local experiments with authentication, file storage, API documentation, and containerized dependencies.
- Intended to show architecture and integration points rather than a hardened deployment model.

## Tech stack

- Backend: Spring Boot, Spring Security, JPA, Flyway, OpenAPI
- Frontend: React
- Infrastructure: PostgreSQL, Keycloak, MinIO, Hazelcast Management Center

## Repository layout

- `pastebin-backend/` - API and persistence layer
- `pastebin-frontend/` - browser UI
- `docker-compose.yml` - local dependencies
- `realm-export.json` - Keycloak realm bootstrap

## Quick start

1. Copy the environment template:

```shell
cp .env.example .env
```

2. Fill in the placeholder values in `.env`.
3. Start the local dependency stack:

```shell
docker compose up -d
```

## Local development
### Backend
```shell
cd pastebin-backend/
./gradlew test
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Frontend
```shell
cd pastebin-frontend/
npm install
npm test
npm start
```

## Useful URLs
- Keycloak: `http://localhost:8282`
- Backend Swagger UI: `http://localhost:8081/api/v1/docs/swagger-ui/index.html`
- Hazelcast Management Center: `http://localhost:8080`
- MinIO Console: `http://localhost:9001`

## Security notes

- Do not use placeholder credentials outside local development.
- Change all `.env` values before the first run.
- This repo is intentionally a demo and should not be treated as a secure production deployment.

## Status

- Demo project.
- No CI is currently configured.
