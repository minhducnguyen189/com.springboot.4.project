# CLAUDE.md — table-with-million-records

Module-scoped guidance. The repo-root `../CLAUDE.md` behavioral rules also apply; `../AGENTS.md` and
`../GEMINI.md` hold longer-form context.

## What this module is
Spring Boot 4 study module: efficient querying of large PostgreSQL tables via **offset** vs **cursor**
pagination. Flyway seeds ~10M `transaction_detail` + 200K `bank_account` rows on first start.

## Stack
Java 25 · Maven 3.9.12 · Spring Boot 4.0.3 · PostgreSQL · Spring Data JPA/Hibernate · Flyway ·
OpenAPI Generator 7.20.0 (contract-first) · MapStruct 1.6.3 · Lombok · JSpecify · JUnit 5 + Mockito.

## Build / Run / Test — run from the **repo root**
This module's `pom.xml` has **no `<parent>`**, so root-only plugins (Spotless) resolve only at the root.
```bash
mvn -pl table-with-million-records -am compile        # regenerates OpenAPI sources
mvn -pl table-with-million-records test               # pure Mockito unit tests — no DB needed
mvn spotless:apply                                    # format (Google Java Format, AOSP); root only
cd table-with-million-records && mvn spring-boot:run  # needs Postgres; first run seeds millions (slow)
```
App: port **7070**, base path **/api** (e.g. `POST /api/private-app/transactions/actions/filter-cursor`).
DB (hardcoded in `application.yaml`): `localhost:5432/postgres`, user `root` / pass `password`.

## Architecture
`Controller → Service → Repository → DB`, with MapStruct for Entity↔Model.
- **Controllers** implement generated `*Api` interfaces, `@RequestMapping("/api")`, delegate to services.
- **Services** hold logic; **constructor-inject repositories only**.
- **Repositories** extend `JpaRepository` + `JpaSpecificationExecutor`.
- **Mappers**: `@Mapper` + static `MAPPER` field; call `XxxMapper.MAPPER.toYyy(...)` — **never inject as beans**.
- **`SpecificationHelper`** (shared, static): builds `Pageable`, QBE `Specification`, cursor predicates.
- **`BaseEntity`** (`@MappedSuperclass`): UUID id + audit fields; `@PrePersist`/`@PreUpdate` stamp `*_by = "SYSTEM"`.
- **`GlobalExceptionHandler`** (`@ControllerAdvice`) → `ErrorResponseModel`. Delete is **soft** (`status = DELETED`).

## OpenAPI-first (source of truth)
Edit `src/main/resources/openapi/open-api.yaml`, then `mvn compile` regenerates into
`target/generated-sources/openapi/java/`:
- API → `com.springboot.project.generated.api`; models → `...generated.model` (suffix `model`,
  e.g. `TransactionDetailModel`, `DomainEnumModel`).
- **Never hand-edit `target/generated-sources/`** — it's wiped on `mvn clean`.

## Pagination (the point of this module)
- **Offset**: `SpecificationHelper.buildPageable` → `repo.findAll(spec, pageable)`; `totalItems` =
  `page.getTotalElements()` (full COUNT — costly at 10M rows).
- **Cursor**: cursor = `sequenceNumber`. `SpecificationHelper.cursorPagination` adds `>`/`<` vs the token;
  `repo.findBy(spec, q -> …limit(pageSize)…)`; `totalItems` = `findMaxSequenceNumber()`; next/prev tokens =
  last/first row `sequenceNumber` (a `previousPageToken` flips the sort).
- **Filtering**: QBE `Example` + `ExampleMatcher` (CONTAINING, ignore-case, ignore-null) → `SpecificationHelper.init`.
- `@Generated` fields (`sequenceNumber`, `transactionNumber`) are DB-generated — don't set them in code.

## Conventions
- Format: Google Java Format **AOSP** (4-space). Test names: snake_case `should_<action>_when_<condition>`.
- Annotation-processor order matters: MapStruct → Lombok → lombok-mapstruct-binding → spring-boot-configuration-processor.

## Gotchas
- **Auth not wired**: `bearerAuth` is in `open-api.yaml` but there's **no `SecurityFilterChain`** → endpoints
  are open; `Authorization: Bearer test` is ignored. `LoginUser*` / `BadCredentialException` are dormant scaffolding.
- **No parent pom**: root **Spotless** + git pre-commit hook don't apply to this module — run Spotless from the
  repo root. The `hooks/pre-commit` the root pom references doesn't exist.
- **Unit tests need no database** (pure Mockito).
- **`test.sh` is stale** (path missing `/api` → 404); use **`test2.sh`** or the Bruno collection under
  `src/main/resources/openapi/`.
- **`GlobalExceptionHandler.handleException`** declares its param as `ResourceNotFoundException` while catching
  `Exception` — a latent bug for other exception types.
