# pastebin-app

> Full-stack demo pastebin-приложения: Spring Boot API, React UI, вход через Keycloak, PostgreSQL для метаданных, MinIO для текста паст и кэширование через Hazelcast.

![Backend](https://img.shields.io/badge/backend-spring%20boot%203.0.10-111827?style=for-the-badge&labelColor=111827&color=5b5ef4)
![Frontend](https://img.shields.io/badge/frontend-react%2018-111827?style=for-the-badge&labelColor=111827&color=5b5ef4)
![Auth](https://img.shields.io/badge/auth-keycloak%2020-111827?style=for-the-badge&labelColor=111827&color=5b5ef4)

[English version](README.md)

| Поле | Значение |
|---|---|
| Статус | Завершенная sandbox-демонстрация |
| Тип | Full-stack demo / integration playground |
| Основной стек | Java 17, Spring Boot 3.0.10, React 18, CRA 5, Keycloak 20, PostgreSQL 14, MinIO, Hazelcast |
| Основной сценарий | Аутентифицированный пользователь создает paste, backend хранит metadata в PostgreSQL и content в MinIO, затем достает paste по hash |

## Назначение проекта

- Demo / sandbox проект, а не production deployment.
- Полезен для локальных экспериментов с JWT/OIDC-аутентификацией, object storage, API-документацией, caching и контейнеризированными зависимостями.
- Показывает архитектуру и точки интеграции, но не претендует на hardened deployment model.

## Быстрый старт

```shell
cp .env.example .env
# Замените placeholder-значения перед первым запуском.
docker compose up -d
```

Backend и frontend запускаются в отдельных терминалах:

```shell
cd pastebin-backend
./gradlew bootRun --args='--spring.profiles.active=dev'
```

```shell
cd pastebin-frontend
npm install
npm start
```

## Матрица сервисов

| Сервис | Путь / image | Runtime/framework | Порт | Ответственность | Команда проверки |
|---|---|---|---:|---|---|
| Frontend | `pastebin-frontend/` | React 18.2.0, CRA 5.0.1, Keycloak JS 20.0.3, PrimeReact 9.3.0 | 3000 | Browser UI, Keycloak login, формы создания/поиска paste | `npm test`, `npm run build` |
| Backend | `pastebin-backend/` | Java 17, Spring Boot 3.0.10, Gradle 8.2.1 | 8081 | REST API, JWT validation, orchestration persistence | `./gradlew test`, `./gradlew build` |
| PostgreSQL | `postgres:14.8-alpine3.18` | Docker Compose | 54322 -> 5432 | Metadata и tags для паст | `docker compose ps postgres` |
| Keycloak DB | `postgres:14.8-alpine3.18` | Docker Compose | 5434 -> 5432 | Persistence для Keycloak | `docker compose ps keycloak-postgres` |
| Keycloak | `quay.io/keycloak/keycloak:20.0.2` | Docker Compose | 8282 -> 8080 | Local OIDC provider, realm `pastebin` | `docker compose ps keycloak` |
| MinIO | `minio/minio` | Docker Compose | 9000, 9001 | Object storage для текста паст | `docker compose ps minio` |
| Hazelcast Management Center | `hazelcast/management-center:5.2.0` | Docker Compose | 8080 | Local cache observability UI | `docker compose ps hazelcast-management-center` |

## Архитектура

```text
React UI (localhost:3000)
  -> Keycloak (localhost:8282, realm pastebin, frontend-client)
  -> Spring Boot API (localhost:8081, Bearer JWT)
       -> PostgreSQL: paste metadata, tags, expiration
       -> MinIO: paste text content under bin/<hash>
       -> Hazelcast: pasteByHash cache and hashQueue
```

Важные границы:

- UI/API calls настроены под локальную разработку: frontend обращается к `http://localhost:8081`, Keycloak работает на `http://localhost:8282`.
- Backend разрешает unauthenticated-доступ только к `/api/v1/docs/**`; остальные запросы требуют Bearer JWT.
- Текст paste хранится в MinIO. PostgreSQL хранит metadata и `content_path`.
- `realm-export.json` присутствует для bootstrap Keycloak realm. `docker-compose.yml` запускает Keycloak с `--import-realm`; если realm не импортировался автоматически в локальной среде, импортируйте этот файл вручную в Keycloak.

## Обзор API

| Метод | Путь | Auth | Описание |
|---|---|---|---|
| `POST` | `/api/v1/paste` | Bearer JWT | Создать paste и вернуть hash |
| `GET` | `/api/v1/paste/{hash}` | Bearer JWT | Загрузить metadata и content paste по hash |
| `GET` | `/api/v1/docs/swagger-ui.html` | Public | Swagger UI |
| `GET` | `/api/v1/docs/api-docs` | Public | OpenAPI JSON |

Форма payload для создания:

```json
{
  "title": "Example",
  "author": "Mark",
  "tags": ["demo", "pastebin"],
  "text": "Paste content",
  "expiration": "N"
}
```

В UI доступны `N`, `10m` и `1M`. Backend также принимает `1Y`, `1H`, `1D`, `1W`, `2W`, `6M` или дату в формате `yyyy-MM-dd`.

## Локальная разработка

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

## Полезные URL

| Компонент | URL | Примечания |
|---|---|---|
| Frontend | `http://localhost:3000` | React development server |
| Backend API | `http://localhost:8081/api/v1/paste` | Требует Bearer JWT |
| Swagger UI | `http://localhost:8081/api/v1/docs/swagger-ui.html` | Public docs path из `application-dev.yml` |
| OpenAPI JSON | `http://localhost:8081/api/v1/docs/api-docs` | Public docs path из `application-dev.yml` |
| Keycloak | `http://localhost:8282` | Local admin/realm UI |
| Hazelcast Management Center | `http://localhost:8080` | Local observability UI |
| MinIO Console | `http://localhost:9001` | Local object storage console |

## Ограничения / безопасность

- Не используйте placeholder credentials вне локальной разработки.
- Держите `.env` локально и замените все placeholder-значения перед первым запуском.
- Local frontend config содержит hardcoded Keycloak и backend URLs; для external deployments нужна отдельная настройка конфигурации.
- Backend отключает CSRF и использует stateless JWT validation для demo API.
- Проект является demo и не должен рассматриваться как безопасный production deployment.
- CI сейчас не настроен.

## Статус

Portfolio/demo project. Код опубликован как architecture and implementation reference, а не как production-ready service.
