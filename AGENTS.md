# Repository Guidelines

## Project Structure & Module Organization

This is a Java 17 Spring Boot application for `ilPaiolo`. Source code lives under `src/main/java/it/uniroma3/siw/progetto`, organized by Spring layer:

- `controller/` contains MVC request handlers.
- `service/` contains business logic.
- `repository/` contains Spring Data JPA repositories.
- `model/` contains entities, enums, and domain objects.

Application resources live in `src/main/resources`. Thymeleaf templates are in `src/main/resources/templates`, grouped by feature when needed, such as `templates/events/lista.html`. Tests live in `src/test/java` using the same package structure as production code.

## Build, Test, and Development Commands

Use the Maven wrapper included in the repository:

- `.\mvnw.cmd test` runs the test suite on Windows.
- `./mvnw test` runs the test suite on Unix-like shells.
- `.\mvnw.cmd spring-boot:run` starts the local application on port `8080`.
- `.\mvnw.cmd clean package` compiles, tests, and builds the packaged artifact in `target/`.

The app expects PostgreSQL at `jdbc:postgresql://localhost:5432/ilPaiolo` with credentials configured in `src/main/resources/application.properties`.

## Coding Style & Naming Conventions

Follow the existing Spring Boot style. Keep Java packages lowercase under `it.uniroma3.siw.progetto`. Use PascalCase for classes and enums (`EventoController`, `StatoGioco`), camelCase for fields and methods, and descriptive Italian domain names consistent with the current codebase.

Use tab indentation in Java files to match the existing project. Keep controllers thin, place business rules in services, and access persistence through repositories rather than directly from controllers.

## Testing Guidelines

Tests use JUnit 5 with Spring Boot Test. Name test classes after the class or feature under test, ending in `Tests`, for example `EventoServiceTests`. Put tests in `src/test/java/it/uniroma3/siw/progetto/...`.

Run `.\mvnw.cmd test` before submitting changes. Add focused tests for service logic, repository behavior, validation rules, and controller flows when those areas change.

## Commit & Pull Request Guidelines

Recent commits are short and informal, but contributors should prefer clear imperative messages such as `Add event registration validation` or `Fix table reservation lookup`.

Pull requests should include a concise description, the affected feature or use case, testing performed, and screenshots when templates or visible UI change. Link related issues or project notes when available, and call out any database, configuration, or security-related changes.

## Security & Configuration Tips

Do not commit real credentials. `application.properties` currently contains local development database settings; use environment-specific overrides for shared or deployed environments. Review changes to Spring Security, validation, and database schema generation carefully because they can affect authentication, user data, and local data persistence.
