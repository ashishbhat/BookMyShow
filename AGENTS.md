# Repository Guidelines

## Project Structure & Module Organization

This is a Java 17 Spring Boot Maven project. Application code lives under `src/main/java/com/example/bookmyshow`.

- `BookmyshowApplication.java` is the Spring Boot entry point.
- `controllers/` contains REST controllers such as `MovieController`.
- `services/` contains business logic and transaction boundaries.
- `repositories/` contains Spring Data JPA repositories.
- `entities/` contains JPA entities and enums.
- `src/main/resources/application.properties` holds runtime configuration.
- `src/test/java/com/example/bookmyshow` contains JUnit/Spring Boot tests.

Static and template directories exist under `src/main/resources`, but the current app is API-focused.

## Build, Test, and Development Commands

Use the Maven wrapper so contributors run the same Maven setup:

```bash
./mvnw spring-boot:run
./mvnw test
./mvnw test -Dtest=BookmyshowApplicationTests
./mvnw package
./mvnw spring-boot:build-image
```

- `spring-boot:run` starts the API locally, usually on port `8080`.
- `test` runs the JUnit test suite.
- `package` builds the executable jar in `target/`.
- `spring-boot:build-image` builds an OCI image using Buildpacks.

## Coding Style & Naming Conventions

Follow standard Java conventions: 4-space indentation, `PascalCase` types, `camelCase` fields and methods, and uppercase enum constants. Keep package names lowercase under `com.example.bookmyshow`.

Use Spring stereotypes consistently: controllers in `controllers`, services in `services`, repositories in `repositories`, and JPA models in `entities`. Prefer constructor injection; Lombok `@RequiredArgsConstructor` is already used. This project uses Jakarta APIs (`jakarta.*`), not legacy `javax.*`.

## Testing Guidelines

Tests use JUnit 5 with Spring Boot test starters. Name test classes with the `*Tests` suffix and keep them in the matching package under `src/test/java`.

Add focused tests for new controller, service, and repository behavior. For web endpoints, prefer Spring MVC tests where possible; use `@SpringBootTest` for full context checks. Run `./mvnw test` before submitting changes.

## Commit & Pull Request Guidelines

This checkout does not include Git history, so no repository-specific commit convention can be inferred. Use concise, imperative commit messages such as `Add movie update endpoint` or `Validate movie rating input`.

Pull requests should include a short description, the commands run for verification, linked issues when applicable, and sample requests/responses or screenshots for user-visible API changes.

## Security & Configuration Tips

Do not commit secrets or local database passwords. Keep environment-specific settings outside source control or pass them through environment variables. JPA is enabled and MySQL is on the runtime classpath, so database-backed changes should document required schema and connection settings.
