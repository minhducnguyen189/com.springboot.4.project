# GEMINI.md — Project Context for AI Assistants

## Project Overview

This is a **Spring Boot multi-module Maven project** focused on studying patterns for handling large-scale data (millions of records) efficiently. The project currently has one module:

| Module                       | Description                                                                                                                    |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| `table-with-million-records` | REST API demonstrating efficient querying of large PostgreSQL tables using offset-based pagination and cursor-based pagination |

> **Note:** The project has a fully implemented layered architecture: controllers, services, repositories, entities, mappers, exception handling, and unit tests are all in place. Authentication endpoints remain stubbed (`TODO`).

---

## Technology Stack

| Layer                    | Technology                           | Version                  |
| ------------------------ | ------------------------------------ | ------------------------ |
| Language                 | Java (AdoptOpenJDK)                  | 25.0.2+10.0.LTS          |
| Framework                | Spring Boot                          | 4.0.3                    |
| Security                 | Spring Boot Starter Security         | 4.0.3                    |
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
| Testing (Security)       | Spring Security Test                 | 6.5.2                    |
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

## Project Structure

```
com.springboot.4.project/              ← root (parent POM)
├── pom.xml                            ← parent pom, manages shared dependencies & plugins
├── GEMINI.md
├── .tool-versions                     ← asdf version pinning (Java 25, Maven 3.9.12)
├── .gitignore
└── table-with-million-records/        ← child module
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/springboot/project/
        │   │   ├── TableWithMillionRecordApp.java       ← Spring Boot entry point
        │   │   ├── controller/
        │   │   │   ├── AuthenticationController.java    ← authentication endpoints (stubbed)
        │   │   │   ├── BankAccountController.java       ← bank account filter endpoints
        │   │   │   ├── LoginUserController.java         ← current user endpoint
        │   │   │   └── TransactionController.java       ← transaction filter endpoints
        │   │   ├── entity/
        │   │   │   ├── BaseEntity.java                  ← @MappedSuperclass (id, audit fields)
        │   │   │   ├── BankAccountEntity.java
        │   │   │   ├── TransactionDetailEntity.java
        │   │   │   ├── LoginUserEntity.java
        │   │   │   ├── converter/
        │   │   │   │   └── UserRolesConverter.java      ← JPA converter for user roles
        │   │   │   └── *EnumEntity.java                 ← 7 enum types (AccountStatus, AccountType, Currency, PaymentMethod, TransactionDomain, TransactionStatus, UserRole)
        │   │   ├── exception/
        │   │   │   ├── BadCredentialException.java
        │   │   │   └── ResourceNotFoundException.java
        │   │   ├── handler/
        │   │   │   └── GlobalExceptionHandler.java      ← @ControllerAdvice for error responses
        │   │   ├── mapper/
        │   │   │   ├── BankAccountMapper.java           ← MapStruct mapper (static MAPPER instance)
        │   │   │   ├── LoginUserMapper.java             ← MapStruct mapper (static MAPPER instance)
        │   │   │   └── TransactionDetailMapper.java     ← MapStruct mapper (static MAPPER instance)
        │   │   ├── model/
        │   │   │   └── LoginUserModel.java              ← internal DTO for authenticated user principal
        │   │   ├── repository/
        │   │   │   ├── BankAccountRepository.java       ← JpaRepository + JpaSpecificationExecutor
        │   │   │   ├── LoginUserRepository.java         ← JpaRepository with findByEmail
        │   │   │   └── TransactionRepository.java       ← JpaRepository + JpaSpecificationExecutor
        │   │   ├── service/
        │   │   │   ├── BankAccountService.java          ← offset & cursor pagination filtering
        │   │   │   ├── LoginUserService.java            ← upsert & getCurrentLoginUser
        │   │   │   └── TransactionService.java          ← offset & cursor pagination filtering
        │   │   └── shared/
        │   │       └── SpecificationHelper.java         ← JPA Specification & pagination utilities
        │   └── resources/
        │       ├── application.yaml
        │       ├── openapi/
        │       │   └── open-api.yaml                              ← API contract (OpenAPI 3.0.3)
        │       └── db/migration/
        │           └── V1_0_20260221180000__initial_table.sql     ← seeds 10M transactions + 200K bank accounts
        ├── test/
        │   └── java/com/springboot/project/service/
        │       ├── BankAccountServiceTest.java          ← 7 tests (offset + cursor pagination)
        │       ├── LoginUserServiceTest.java            ← 6 tests (upsert + getCurrentUser scenarios)
        │       └── TransactionServiceTest.java          ← 7 tests (offset + cursor pagination)
        └── generated/                 ← auto-generated by openapi-generator-maven-plugin (cleaned on mvn clean)
```

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

## Database Schema

Three main tables managed by Flyway migrations:

- **`login_user`** — application users with roles (`email`, `username`, `name`, `phone`, `roles`, audit columns)
- **`bank_account`** — 200,000 seeded accounts (`first_name`, `last_name`, contact info, `account_number`, `account_type` [SAVINGS/CURRENT], `ifsc_code`, `balance`, `currency` [USD], `status` [ACTIVE/INACTIVE/CLOSED])
- **`transaction_detail`** — 10,000,000 seeded transactions linked to bank accounts (`transaction_number`, `date`, `domain`, `location`, `value`, `status`, `payment_method`, `tax_amount`, `net_value`, `bank_account_id` FK)

### Entity Enums

| Enum Entity                   | Values                                                                     |
| ----------------------------- | -------------------------------------------------------------------------- |
| `AccountStatusEnumEntity`     | ACTIVE, INACTIVE, CLOSED                                                   |
| `AccountTypeEnumEntity`       | SAVINGS, CURRENT                                                           |
| `CurrencyEnumEntity`          | USD                                                                        |
| `PaymentMethodEnumEntity`     | CARD, CASH, UPI                                                            |
| `TransactionDomainEnumEntity` | EDUCATION, INTERNATIONAL, INVESTMENTS, MEDICAL, PUBLIC, RESTAURANT, RETAIL |
| `TransactionStatusEnumEntity` | SUCCESS, FAILED, CANCELLED                                                 |
| `UserRoleEnumEntity`          | ADMIN, USER                                                                |

### BaseEntity

All entities extend `BaseEntity` which provides:

- `id` (UUID, auto-generated)
- `createdAt`, `createdBy`, `updatedAt`, `updatedBy` (audit columns)
- `@PrePersist` hook that sets audit fields from `SecurityContextHolder`

Sequences used:

- `transaction_number_seq` — generates `TRN000...` formatted transaction numbers (13-digit zero-padded)
- `sequence_number` — numeric sequence for transaction ordering
- `bank_account_sequence_number` — numeric sequence for account ordering

### Seed Data Details

The migration uses a single DO block to:

1. Insert 10M `transaction_detail` rows with random dates spanning 5 years, random domains, random values (500–50,000), random statuses/payment methods, and computed tax/net values
2. Insert 200K `bank_account` rows with random names/addresses/balances
3. Assign `bank_account_id` foreign keys to all transactions (round-robin)
4. Set `bank_account_id` to NOT NULL after all assignments

---

## API Endpoints

Base URL: `/api`

| Tag            | Method | Path                                               | Description                                          |
| -------------- | ------ | -------------------------------------------------- | ---------------------------------------------------- |
| authentication | GET    | `/private-app/actions/authenticate`                | Redirect login (optional `redirectPath` query param) |
| authentication | GET    | `/private-app/actions/refresh-token`               | Refresh token redirect                               |
| login-user     | GET    | `/private-app/users/actions/current`               | Get current logged-in user                           |
| transaction    | POST   | `/private-app/transactions/actions/filter`         | Filter transactions (offset pagination)              |
| transaction    | POST   | `/private-app/transactions/actions/filter-cursor`  | Filter transactions (cursor pagination)              |
| bank-account   | POST   | `/private-app/bank-accounts/actions/filter`        | Filter bank accounts (offset pagination)             |
| bank-account   | POST   | `/private-app/bank-accounts/actions/filter-cursor` | Filter bank accounts (cursor pagination)             |

### API Models (from OpenAPI spec)

| Schema                      | Description                                                                                                                                |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| `LoginUserResponse`         | User info: id (UUID), email, username, name, roles (ADMIN/USER)                                                                            |
| `TransactionFilterRequest`  | Filter by date, domain, location, value, status, paymentMethod, taxAmount, netValue + `PaginationRequest`                                  |
| `TransactionFilterResponse` | Paginated list of `TransactionDetail` with foundItems, totalItems, previousPageToken, nextPageToken                                        |
| `TransactionDetail`         | Full transaction record (id, transactionNumber, sequenceNumber, date, domain, location, value, status, paymentMethod, taxAmount, netValue) |
| `BankAccountFilterRequest`  | Filter by firstName, lastName, phone, email, accountNumber, accountType, ifscCode, status + `PaginationRequest`                            |
| `BankAccountFilterResponse` | Paginated list of `BankAccountDetail` with foundItems, totalItems, previousPageToken, nextPageToken                                        |
| `BankAccountDetail`         | Full account record (id, sequenceNumber, name, contact, address, accountNumber, accountType, ifscCode, balance, currency, status)          |
| `PaginationRequest`         | pageSize, pageNumber, sortBy, sortOrder (ASC/DESC), previousPageToken, nextPageToken                                                       |
| `ErrorResponse`             | Standard error: code, status, message, path, timestamp                                                                                     |

### Enums (Generated Models)

| Enum                    | Values                                                                     |
| ----------------------- | -------------------------------------------------------------------------- |
| `DomainEnum`            | EDUCATION, INTERNATIONAL, INVESTMENTS, MEDICAL, PUBLIC, RESTAURANT, RETAIL |
| `TransactionStatusEnum` | SUCCESS, FAILED, CANCELLED                                                 |
| `PaymentMethodEnum`     | CARD, CASH, UPI                                                            |
| `UserRoleEnum`          | ADMIN, USER                                                                |
| `SortOrderEnum`         | ASC, DESC                                                                  |
| `AccountTypeEnum`       | SAVINGS, CURRENT                                                           |
| `CurrencyEnum`          | USD                                                                        |
| `AccountStatusEnum`     | ACTIVE, INACTIVE, CLOSED                                                   |

Swagger UI is available at: `http://localhost:7070/swagger-ui/index.html`

---

## Testing

### Test Framework

- **JUnit 5** (via `spring-boot-starter-test`) + **Mockito Core 5.21.0**
- **Spring Security Test** for mocking `SecurityContextHolder`
- Test naming convention: **snake_case** following `should_<action>_when_<condition>` pattern

### Test Coverage

| Test Class               | Tests | Covers                                                                                           |
| ------------------------ | ----- | ------------------------------------------------------------------------------------------------ |
| `BankAccountServiceTest` | 7     | Offset pagination (filter, empty, sorting) + Cursor pagination (no token, next, previous, empty) |
| `TransactionServiceTest` | 7     | Offset pagination (filter, empty, sorting) + Cursor pagination (no token, next, previous, empty) |
| `LoginUserServiceTest`   | 6     | Upsert (create/update) + getCurrentUser (authenticated, not found, null auth, wrong principal)   |

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
- **Bearer token security**: Endpoints (except authentication) require `bearerAuth` security scheme
- **Global Exception Handling**: `@ControllerAdvice` returns standardized `ErrorResponseModel`

---

## Environment Configuration

`src/main/resources/application.yaml`:

```yaml
server:
  port: "7070"
  forward-headers-strategy: "framework"
spring:
  datasource:
    driverClassName: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/postgres?useUnicode=true&characterEncoding=UTF-8
    username: root
    password: password
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
    open-in-view: true
    properties:
      hibernate:
        generate_statistics: true
  flyway:
    enabled: true
    table: "flyway_schema_history"
    baseline-on-migrate: true
    baseline-version: 0
```
