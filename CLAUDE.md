# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project status

Fresh Spring Initializr scaffold — only `BookmyshowApplication` (the `@SpringBootApplication` entry point) and a `contextLoads` smoke test exist. No domain code, controllers, entities, or repositories yet. Treat new feature work as greenfield: pick a package layout under `com.example.bookmyshow` deliberately, since none is established.

## Stack

- Spring Boot **4.1.0-RC1** (pre-release) on **Java 17**, built with the bundled Maven Wrapper.
- Starters present: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-actuator` (plus matching `*-test` starters).
- No JDBC driver is on the classpath yet. Adding JPA-backed code without also adding a driver (e.g. H2 for dev, Postgres/MySQL for prod) and DB config in `application.properties` will fail at startup because Spring Boot autoconfigures a `DataSource`.
- Spring Boot 4.x uses Jakarta EE 10+ namespaces (`jakarta.*`, not `javax.*`) — relevant when copying snippets from older tutorials.

## Commands

Use the wrapper (`./mvnw`) so the project's Maven version is used.

```bash
./mvnw spring-boot:run                                  # run the app (default port 8080)
./mvnw test                                             # run all tests
./mvnw test -Dtest=BookmyshowApplicationTests           # run a single test class
./mvnw test -Dtest=BookmyshowApplicationTests#contextLoads   # run a single test method
./mvnw package                                          # build executable jar into target/
./mvnw spring-boot:build-image                          # build OCI image via Buildpacks
```

Actuator is on the classpath; once the app runs, `/actuator/health` is available by default.
