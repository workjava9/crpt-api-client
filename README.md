# Crpt API Client

Клиентская библиотека на **Java 21 + Spring Boot 3** для работы с API [Честный Знак](https://честныйзнак.рф).  
Поддерживает **ограничение числа запросов** (rate limiting) и потокобезопасность.

## Возможности
- Создание документов для ввода в оборот товаров (с подписями и сериализацией в JSON).
- Rate limiter с настройкой количества запросов на интервал (`TimeUnit` + `requestLimit`).
- Thread-safe реализация (безопасно в многопоточной среде).
- Простое расширение под другие методы API.
- Поддержка Docker/Docker Compose для локального запуска и тестирования.

## Стек технологий
- Java 21
- Spring Boot 3.3.x
- Gradle Kotlin DSL
- Lombok
- WireMock (для интеграционных тестов)
- JUnit 5 + AssertJ
- Docker + Docker Compose
- GitHub Actions (CI/CD)

## Запуск

### 1. Сборка проекта
```bash
./gradlew clean build
```

### 2. Запуск тестов
```bash
./gradlew test
```

### 3. Запуск через Docker
```bash
docker compose up --build
```

## Структура проекта
```
crpt-api-client/
 ├── src/main/java/dev/lab/crpt/
 │    ├── CrptApi.java         # Основной клиент
 │    ├── RateLimiter.java     # Ограничение числа запросов
 │    └── model/               # DTO для API
 ├── src/test/java/dev/lab/crpt/
 │    ├── CrptApiTest.java
 │    └── CrptClientWireMockIT.java
 ├── build.gradle.kts
 ├── settings.gradle.kts
 ├── Dockerfile
 ├── docker-compose.yml
 └── README.md
```

## Тестирование
- Unit-тесты для rate limiter.
- Интеграционные тесты с **WireMock** (мок API Честного знака).
- Поддержка GitHub Actions (автоматический прогон тестов + сборка Docker-образов).
