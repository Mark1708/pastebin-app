# pastebin-app

Full-stack demo pastebin-приложения с backend на Spring Boot, frontend на React, аутентификацией через Keycloak, PostgreSQL, MinIO и Hazelcast Management Center.

[English version](README.md)

## Назначение проекта

- Demo / sandbox проект, а не production-ready сервис.
- Полезен для локальных экспериментов с аутентификацией, хранением файлов, API-документацией и контейнеризированными зависимостями.
- Показывает архитектуру и точки интеграции, но не претендует на hardened deployment model.

## Стек

- Backend: Spring Boot, Spring Security, JPA, Flyway, OpenAPI
- Frontend: React
- Infrastructure: PostgreSQL, Keycloak, MinIO, Hazelcast Management Center

## Структура репозитория

- `pastebin-backend/` - API и persistence layer
- `pastebin-frontend/` - browser UI
- `docker-compose.yml` - локальные зависимости
- `realm-export.json` - bootstrap Keycloak realm

## Быстрый старт

1. Скопировать шаблон окружения:

```shell
cp .env.example .env
```

2. Заменить placeholder-значения в `.env`.
3. Запустить локальный стек зависимостей:

```shell
docker compose up -d
```

## Локальная разработка

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

## Полезные URL

- Keycloak: `http://localhost:8282`
- Backend Swagger UI: `http://localhost:8081/api/v1/docs/swagger-ui/index.html`
- Hazelcast Management Center: `http://localhost:8080`
- MinIO Console: `http://localhost:9001`

## Security notes

- Не используйте placeholder credentials вне локальной разработки.
- Замените все значения `.env` перед первым запуском.
- Проект является demo и не должен рассматриваться как безопасный production deployment.

## Статус

- Demo project.
- CI сейчас не настроен.
