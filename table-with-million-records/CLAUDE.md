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

## Architecture — package-by-feature
Organised by feature under `com.springboot.project`; flow per feature is
`Controller → IService → Service → Repository → DB`, with MapStruct for Entity↔Model.
```
common/       entity(BaseEntity) · exception(ResourceNotFoundException, BadRequestException,
              BadCredentialException) · handler(GlobalExceptionHandler) · specification(SpecificationHelper)
              · generated.model (shared DTOs)
bankaccount/  controller · service(IBankAccountService, BankAccountService) · repository · entity · mapper
              · generated.{api,model}
transaction/  controller · service(ITransactionService, TransactionService) · repository · entity · mapper
              · generated.{api,model}
loginuser/    entity(+converter) · model · mapper · repository   (dormant scaffolding; no controller/service)
```
- **Controllers** implement generated `*Api`, `@RequestMapping("/api")`, constructor-inject the feature `IService`.
- **Services** implement `I<Feature>Service`, hold logic, and carry `@Transactional` (writes) /
  `@Transactional(readOnly = true)` (reads); constructor-inject Spring Data repositories directly (no port/adapter).
- **Repositories** extend `JpaRepository` + `JpaSpecificationExecutor`.
- **Mappers**: `@Mapper` + static `MAPPER` field; call `XxxMapper.MAPPER.toYyy(...)` — **never inject as beans**.
- **`SpecificationHelper`** (`common.specification`, static): builds `Pageable`, QBE `Specification`, cursor
  predicates, the shared `containingIgnoreCaseMatcher`, and `buildCursorQuery`/`limitedSortedQuery` +
  the `CursorQuery` record that de-duplicate cursor paging across the two services.
- **`BaseEntity`** (`common.entity`, `@MappedSuperclass`): UUID id + audit fields; `@PrePersist`/`@PreUpdate` stamp `*_by = "SYSTEM"`.
- **`GlobalExceptionHandler`** (`@ControllerAdvice`) → `ErrorResponseModel`, mapping `ResourceNotFoundException`
  → 404, `BadRequestException` → 400, other `Exception` → 500 (each via `@ResponseStatus`). Delete is **soft** (`status = DELETED`).
- **Cross-feature**: `TransactionService` injects `bankaccount.repository.BankAccountRepository` directly to
  resolve the `@ManyToOne` bank account; `transaction.entity` ↔ `bankaccount.entity` are JPA-related across features.

## OpenAPI-first (source of truth) — 3 specs, 3 generator executions
Specs live in `src/main/resources/openapi/`: **`common.yaml`** (shared: `PaginationRequest`, `SortOrderEnum`,
`ErrorResponse`, `LoginUserResponse`, `UserRoleEnum`), **`bank-account.yaml`**, **`transaction.yaml`**
(feature paths + schemas; shared types referenced via external `$ref: "./common.yaml#/components/schemas/..."`).
Edit a spec, then `mvn compile` regenerates into `target/generated-sources/openapi/java/`:
- **common** → models only into `com.springboot.project.common.generated.model` (suffix `model`).
- **bank-account / transaction** → `...<feature>.generated.{api,model}` (`*Api` + `ApiUtil.java`, suffix `model`).
- Shared types are **generated once** by `common` and **imported, not regenerated**, by the feature executions
  via **`schemaMappings`** (suppress regeneration, render simple name) **+ `importMappings`** (import the common
  class). This is load-bearing; the compile is the gate.
- **Never hand-edit `target/generated-sources/`** — it's wiped on `mvn clean`.

## Pagination (the point of this module)
- **Offset**: `SpecificationHelper.buildPageable` → `repo.findAll(spec, pageable)`; `totalItems` =
  `page.getTotalElements()` (full COUNT — costly at 10M rows).
- **Cursor**: cursor = `sequenceNumber`. `SpecificationHelper.buildCursorQuery` narrows the base spec with
  `cursorPagination` (`>`/`<` vs the token, flipping sort for a `previousPageToken`) and returns a `CursorQuery`
  (spec + sort + pageSize); `repo.findBy(spec, SpecificationHelper.limitedSortedQuery(sort, pageSize))`;
  `totalItems` = `findMaxSequenceNumber()`; next/prev tokens = last/first row `sequenceNumber`.
- **Filtering**: QBE `Example` + `SpecificationHelper.containingIgnoreCaseMatcher()` (CONTAINING, ignore-case,
  ignore-null) → `SpecificationHelper.init`. Filter methods reject a null `pagination` with `BadRequestException` (400).
- `@Generated` fields (`sequenceNumber`, `transactionNumber`) are DB-generated — don't set them in code.

## Conventions
- Format: Google Java Format **AOSP** (4-space). Test names: snake_case `should_<action>_when_<condition>`.
- Annotation-processor order matters: MapStruct → Lombok → lombok-mapstruct-binding → spring-boot-configuration-processor.

## Gotchas
- **Auth not wired**: `bearerAuth` is in the specs but there's **no `SecurityFilterChain`** → endpoints
  are open; `Authorization: Bearer test` is ignored. `loginuser/*` / `BadCredentialException` are dormant scaffolding.
- **No parent pom**: root **Spotless** + git pre-commit hook don't apply to this module — run Spotless from the
  repo root. The `hooks/pre-commit` the root pom references doesn't exist.
- **Unit tests need no database** (pure Mockito): `bankaccount`/`transaction` service tests +
  `common.handler.GlobalExceptionHandlerTest`.
- **`test.sh` is stale** (path missing `/api` → 404); use **`test2.sh`** or the Bruno collection under
  `src/main/resources/openapi/`.
