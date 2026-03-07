# GEMINI.md — Project Context for AI Assistants

## Project Overview

This is a **Spring Boot multi-module Maven project** focused on studying patterns for handling large-scale data (millions of records) efficiently. The project currently has one module:

| Module                       | Description                                                                                                                                            |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `table-with-million-records` | REST API demonstrating efficient querying and creation of records in large PostgreSQL tables using offset-based pagination and cursor-based pagination |

> **Note:** The project has a fully implemented layered architecture: controllers, services, repositories, entities, mappers, exception handling, and unit tests are all in place.

---

## Technology Stack

| Layer                    | Technology                           | Version                  |
| ------------------------ | ------------------------------------ | ------------------------ |
| Language                 | Java (AdoptOpenJDK)                  | 25.0.2+10.0.LTS          |
| Framework                | Spring Boot                          | 4.0.3                    |
| Database                 | PostgreSQL (driver)                  | 42.7.10                  |
| ORM                      | Spring Data JPA / Hibernate          | via Spring Boot          |
| Migrations               | Flyway                               | 12.0.2                   |
| API Spec                 | OpenAPI Generator (spring)           | 7.20.0                   |
| API Docs                 | SpringDoc OpenAPI (Swagger UI)       | 3.0.1                    |
| Swagger Annotations      | io.swagger.core.v3                   | 2.2.43                   |
| Mapping                  | MapStruct                            | 1.6.3                    |
| Boilerplate              | Lombok                               | 1.18.42                  |
| Formatting               | Spotless + Google Java Format (AOSP) | 2.44.4 / 1.26.0          |
| Testing                  | JUnit 5 + Mockito Core               | via Spring Boot / 5.21.0 |
| Null Safety              | JSpecify                             | 1.0.0                    |
| Utilities                | Apache Commons Lang3                 | 3.20.0                   |
| Utilities                | Apache Commons BeanUtils             | 1.11.0                   |
| Logging                  | SLF4J                                | 2.0.9                    |
| Build                    | Maven                                | 3.9.12                   |
| Spring Boot Maven Plugin | (repackage & build-info)             | 4.0.3                    |
| Maven Compiler Plugin    |                                      | 3.15.0                   |

### Version Management

A `.tool-versions` file (for [asdf](https://asdf-vm.com/) or compatible tools) pins:

- `java adoptopenjdk-25.0.2+10.0.LTS`
- `maven 3.9.12`

---

## Application Architecture

### Layered Architecture

```
Controller → Service → Repository → Database
     ↕            ↕
  Generated    Mapper (MapStruct)
  API Models   Entity ↔ Model conversion
```

- **Controllers** implement generated OpenAPI interfaces directly (not delegates) and delegate to services
- **Services** contain business logic, use constructor-injected repositories and static mapper instances
- **Repositories** extend `JpaRepository` + `JpaSpecificationExecutor` for dynamic querying
- **Mappers** use `@Mapper` with a static `MAPPER` field (`Mappers.getMapper(...)`) — accessed statically in services, **never injected as Spring beans**
- **SpecificationHelper** provides reusable static methods for building JPA `Specification` and `Pageable` objects
- **GlobalExceptionHandler** (`@ControllerAdvice`) returns standardized `ErrorResponseModel` for exceptions

### Key Patterns

- **Constructor Injection**: All services use `@Autowired` constructor injection for **repositories only** — mappers are never injected
- **MapStruct Static Usage**: Mappers are annotated with `@Mapper` (default component model) and accessed via the static `MAPPER = Mappers.getMapper(...)` field. Services call `BankAccountMapper.MAPPER.toXxx(...)` directly
- **JPA Specification API**: Dynamic queries built via `Specification<T>` for flexible filtering
- **Query by Example (QBE)**: Filter requests are mapped to entity examples via MapStruct, then converted to Specifications
- **Cursor Pagination**: Uses `sequenceNumber` field as cursor token with `greaterThan`/`lessThan` predicates
- **Offset Pagination**: Standard Spring Data `PageRequest` with optional sort

---

## Testing

### Test Framework

- **JUnit 5** (via `spring-boot-starter-test`) + **Mockito Core 5.21.0**
- Test naming convention: **snake_case** following `should_<action>_when_<condition>` pattern

### Test Coverage

| Test Class               | Tests | Covers                                                                                           |
| ------------------------ | ----- | ------------------------------------------------------------------------------------------------ |
| `BankAccountServiceTest` | 7     | Offset pagination (filter, empty, sorting) + Cursor pagination (no token, next, previous, empty) |
| `TransactionServiceTest` | 7     | Offset pagination (filter, empty, sorting) + Cursor pagination (no token, next, previous, empty) |

### Running Tests

```bash
# Run all tests
mvn test

# Run service tests only
mvn test -pl table-with-million-records -Dtest="com.springboot.project.service.*Test"
```

---

## Build & Run

### Prerequisites

- Java 25+ (pinned via `.tool-versions`: `adoptopenjdk-25.0.2+10.0.LTS`)
- Maven 3.9+ (pinned via `.tool-versions`: `3.9.12`)
- PostgreSQL running locally on port `5432`
  - Database: `postgres`
  - Username: `root`
  - Password: `password`

### Common Commands

- Always run all commands in "zsh" shell

```bash
# Clean and compile (also runs Spotless formatting check)
mvn clean compile

# Run tests
mvn test

# Package (skip tests)
mvn clean package -DskipTests

# Apply Spotless formatting
mvn spotless:apply

# Check Spotless formatting
mvn spotless:check

# Clean generated sources
mvn clean  # maven-clean-plugin also deletes src/generated/
```

### Running the Application

```bash
cd table-with-million-records
mvn spring-boot:run
```

Server starts on **port 7070**.

### Database Migration

Flyway runs automatically on startup. The initial migration seeds:

- **10 million** random `transaction_detail` rows (5-year date range)
- **200,000** random `bank_account` rows

> ⚠️ The first startup migration can take several minutes due to the large data seed.

---

## Code Generation

The OpenAPI generator plugin generates interfaces and models from `open-api.yaml` into `target/generated-sources/openapi/java/`:

- API interfaces → `com.springboot.project.generated.api`
- Models → `com.springboot.project.generated.model` (suffix: `model`)

Generated code is cleaned on every `mvn clean` cycle and regenerated on compile. **Do not manually edit files in `target/generated-sources/`.**

---

## Code Style & Conventions

- **Formatter**: Google Java Format (**AOSP** style, 4-space indent)
- **Spotless** enforces formatting on `src/main/java` and `src/test/java`
- A **pre-commit Git hook** (`hooks/pre-commit`) is installed by `git-build-hook-maven-plugin` to enforce formatting before commits
- **MapStruct** is used for DTO ↔ entity mapping — annotate mappers with `@Mapper` and use the static `MAPPER` field (never inject as Spring beans)
- **Lombok** is used to reduce boilerplate — prefer `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` as appropriate
- **Test method naming**: snake*case following `should*<action>_when_<condition>` pattern

### Annotation Processor Order (important)

The compiler plugin annotation processor path order must be: **MapStruct → Lombok → lombok-mapstruct-binding → spring-boot-configuration-processor**. This order ensures MapStruct processes Lombok-generated getters/setters correctly.

---

## Key Design Patterns

- **OpenAPI-first**: API contracts are defined in `open-api.yaml`; server-side interfaces are generated — controllers implement the generated interfaces directly
- **Constructor Injection**: All services and controllers use `@Autowired` constructor injection for **repositories only**
- **MapStruct Static Mappers**: Mappers use default `@Mapper` annotation and are accessed via static `MAPPER` field — never injected as Spring beans
- **Offset pagination** vs **Cursor pagination**: Both strategies are exposed for benchmarking performance on large datasets
- **JPA Specifications + QBE**: Dynamic filtering combines Query by Example with JPA Specification API
- **Flyway migrations**: Versioned SQL files in `src/main/resources/db/migration/`
- **Bearer token security**: Endpoints require `bearerAuth` security scheme
- **Global Exception Handling**: `@ControllerAdvice` returns standardized `ErrorResponseModel`

---
