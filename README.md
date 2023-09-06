# pastebin-app

# Start docker compose
```shell
docker compose up -d
```

# Upload realm config
1. Log In `http://localhost:8282` admin:admin
2. Open clients tab and regenerate credentials
3. Change client credentials in application.yml

# Start backend
```shell
cd pastebin-backend/ && ./gradlew bootRun --args='--spring.profiles.active=dev'
```

# Start frontend
```shell
cd ../pastebin-backend/ && npm install && npm start
```

# Links
* Keycloak - `http://localhost:8282`
* Backend Swagger API - `http://localhost:8081/api/v1/docs/swagger-ui/index.html`
* Hazelcast Manager - `http://localhost:8080`
* MinIO - `http://localhost:9001`