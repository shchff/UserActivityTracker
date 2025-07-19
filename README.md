# UserActivityTracker


В проекте реализованы три микросервиса:

- **producer-service** - публикует события пользователей в Kafka
- **consumer-service** - принимает события из Kafka и сохраняет их в Cassandra
- **query-service** - предоставляет REST API для запросов данных из Cassandra

---

## Запуск

### 1. Kafka и Cassandra через docker-compose

Перейти в папку docker и запустить там скрипт:

```bash
docker-compose up -d
```

После запусков контейнеров создать модель данных:

```bash
docker exec -it cassandra cqlsh -f /init/init.cql
```

### 2. Запуск микросервисов

В каждой из папок микросервисов (`producer-service`, `consumer-service`, `query-service`) запустить `spring-boot` приложение:

Windows:
```shell
.\mvnw spring-boot:run
```

Unix:
```bash
mvn spring-boot:run
```

---
## Использование

Endpoint для создания пользовательского события доступен по адресу `http://localhost:8081/events`:

```text
POST /events
```
Пример `body`:

```json
{
    "userId": "some_user_id",
    "eventType": "LOGIN",
    "metadata": {
      "ip": "192.168.0.1",
      "device": "windows"
    }
}
```

Для анализа событий доступен Swagger UI по адресу: `http://localhost:8083/swagger-ui/index.html`
