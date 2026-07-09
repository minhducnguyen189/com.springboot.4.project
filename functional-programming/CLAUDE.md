# CLAUDE.md — functional-programming

Module-scoped guidance. The repo-root `../CLAUDE.md` behavioral rules also apply; `../AGENTS.md` and
`../GEMINI.md` hold longer-form context.

## What this module is
Spring Boot 4 study module exploring **functional-programming patterns** over a CRUD/REST service for
`bank_account` + `transaction_detail`. Organised **OpenAPI-first** and **package-by-feature**: each
feature is a vertical slice (own OpenAPI spec + own package with controller/service/repository/entity/
mapper/model/validation). FP focus: a composable validation DSL, higher-order pagination, repository
ports + adapters, and a three-tier Dto↔Model↔Entity type model.

## Stack
Java 25 · Maven 3.9.12 · Spring Boot 4.0.3 · PostgreSQL · Spring Data JPA/Hibernate · Flyway ·
OpenAPI Generator 7.20.0 (contract-first, **3 executions**) · MapStruct 1.6.3 · Lombok · JSpecify ·
JUnit 5 + Mockito + AssertJ.

## Build / Run / Test — run from the **repo root**
Registered in the root reactor, but this module's `pom.xml` has **no `<parent>`**, so the root's
Spotless/AOSP config and git pre-commit hook are **not inherited** here.
```bash
mvn -pl functional-programming -am clean compile  # regenerates all 3 specs, then compiles
mvn -pl functional-programming test               # pure Mockito/AssertJ unit test — no DB needed
cd functional-programming && mvn spring-boot:run  # needs Postgres; first run seeds ~1M rows (slow)
```
App: port **7070**, base path **/api**, routes under `/private-app/...`
(e.g. `POST /api/private-app/bank-accounts/actions/filter-cursor`). Entrypoint: `FunctionalProgrammingApp`
(stays at root package `com.springboot.project` so component/entity scan covers every feature sub-package).
DB (hardcoded in `application.yaml`): `localhost:5432/functional-app`, user `root` / pass `password`.
Flyway `V1_0_*__initial_table.sql` seeds **1,000,000** `transaction_detail` rows + tops `bank_account`
up to **20,000** via `generate_series`.

## Package-by-feature layout (`com.springboot.project`)
```
common/        entity(BaseEntity) · exception · handler(GlobalExceptionHandler) · validation(Validation,
               Validations) · specification(SpecificationHelper) · repository(GenericRepository)
               · generated.dto (shared DTOs)
bankaccount/   controller · mapper · service · model · repository · entity · generated.{api,dto}
transaction/   controller · mapper · service · model · validation · repository · entity
               · generated.{api,dto}
```
**Architecture (per feature):** `Controller → Service → Repository port → Adapter → Spring Data → DB`.
- **Controllers** implement the feature's generated `*Api`, `@RequestMapping("/api")`, delegate to the service.
- **Services** (`@Service`) hold logic; **constructor-inject repository PORTS only** (never Spring Data repos).
- **Ports** (`<feature>/repository/I*Repository`) are hand-rolled interfaces extending the empty marker
  `common.repository.GenericRepository`; they declare only what the service uses.
- **Adapters** (`<feature>/repository/*RepositoryAdapter`, `@Component`) wrap the Spring Data repos
  (`<feature>/repository/*Repository extends JpaRepository + JpaSpecificationExecutor`) and delegate.
- **Soft delete**: bank account → `status = CLOSED`; transaction → `status = DELETED`.
- **Cross-feature (preserved):** `transaction.entity.TransactionDetailEntity` `@ManyToOne` ↔
  `bankaccount.entity.BankAccountEntity` `@OneToMany`; `transaction.repository.TransactionBankAccount\
  RepositoryAdapter` injects `bankaccount.repository.BankAccountRepository`.

## OpenAPI-first: 3 specs, 3 generator executions
Specs live in `src/main/resources/openapi/`: **`common.yaml`** (shared schemas: `PaginationRequest`,
`SortOrderEnum`, `ErrorResponse`), **`bank-account.yaml`**, **`transaction.yaml`** (feature paths +
schemas; reference shared types via external `$ref: "./common.yaml#/components/schemas/..."`).
The pom has one generator `<execution>` per spec:
- **common** → models only (`generateApis/SupportingFiles=false`) into `com.springboot.project.common.generated.dto`.
- **bank-account / transaction** → `apiPackage`/`modelPackage` = `...<feature>.generated.{api,dto}`,
  `ApiUtil.java`, `modelNameSuffix=dto`.
- Shared types are **generated once** (by `common`) and **imported, not regenerated**, by the feature
  executions via **`schemaMappings`** (suppress regeneration, render simple name) **+ `importMappings`**
  (import the common class). This is load-bearing — see Gotchas. Edit a spec, then `mvn compile`
  regenerates into `target/generated-sources/openapi/java/`. **Never hand-edit `target/`.**

## Three type tiers + two mapper layers
1. **Generated `*Dto`** — feature DTOs in `<feature>.generated.dto`; shared (`PaginationRequestDto`,
   `ErrorResponseDto`, `SortOrderEnumDto`) in `common.generated.dto`.
2. **`*Model`** — hand-written service-layer types in `<feature>/model` (Lombok `@Builder`); they import
   generated DTO enums + the shared `PaginationRequestDto`.
3. **`*Entity`** — JPA entities in `<feature>/entity`.
Mappers are MapStruct with a static `MAPPER` field (`XxxMapper.MAPPER.toYyy(...)`, **never inject as beans**):
`<feature>/mapper/*DtoMapper` (Dto↔Model) and `*ModelMapper` (Model↔Entity).

## The functional-programming patterns (the point of this module)
- **Validation DSL** (`common/validation/`):
  - `Validation<T>` — custom `@FunctionalInterface` (like `Consumer<T>`): `accept(T)`, `andThen(...)`,
    and a fluent `andField(T value)` that calls `accept(value)` then returns `this`, so one
    validator chains across many fields, ending with `.accept(lastField)`.
  - `Validations` (final, static factories) returns `Validation<T>` lambdas that throw
    `BadRequestException`: `itemMustNotBeNull`, `stringMustNotBeBlank`, `stringMustMatch{Email,Phone,Ifsc}Pattern`,
    `numberMustBe{Positive,NonNegative}`, `itemMustSatisfy(Predicate,msg)`. Pattern/number validators
    **skip nulls** (optional fields); `itemMustNotBeNull`/`stringMustNotBeBlank` are the strict ones.
- **Higher-order pagination**: each service shares one
  `executeFilter(request, Function<PaginationRequestDto,Pageable>)`; offset passes
  `SpecificationHelper::buildPageable`, cursor passes `p -> buildPageableForCursor(p, "sequenceNumber")`.
- **Filtering**: QBE `Example` + `ExampleMatcher` (CONTAINING, ignore-case, ignore-null) → `SpecificationHelper.init`.

## Gotchas
- **Shared-type generator config is load-bearing.** Models import `PaginationRequestDto` and MapStruct
  maps `pagination` field-to-field, so the generated `*FilterRequestDto.pagination` and the model's
  `pagination` must be the **same** `common.generated.dto.PaginationRequestDto`. Achieved with
  `schemaMappings` (simple name, suppresses regeneration) **+** `importMappings` (FQN import). Note:
  `schemaMappings` alone inlines a fully-qualified name and JSpecify's `@Nullable` (TYPE_USE) then fails
  to compile (`type annotation not expected here`); `typeMappings` alone fixes rendering but does **not**
  suppress regeneration (duplicates the shared DTOs per feature). The compile is the gate.
- **Cursor pagination is incomplete**: `filter*WithCursor` only changes the `Pageable` (sort + page 0)
  via `buildPageableForCursor`. `SpecificationHelper.cursorPagination` and next/prev tokens are **never
  used**. `totalItems` is always `page.getTotalElements()` (full COUNT — costly at ~1M rows).
- **`GenericRepository<T>`** (`common.repository`) is an empty marker interface — the base of every
  feature repository port; it declares no methods.
- **Bank-account filter input is unvalidated**: `BankAccountService` filtering runs no request
  validation, whereas `TransactionService.executeFilter` calls `TransactionFilterRequestValidation.validate()`.
- **Services are transactional**: write methods carry `@Transactional`, reads `@Transactional(readOnly = true)`.
- **`GlobalExceptionHandler`** maps `ResourceNotFoundException` → 404, `BadRequestException` → 400
  (all `Validations` failures), and any other `Exception` → 500, each via `@ResponseStatus`.
- **Formatting not enforced here**: root Spotless (Google Java Format AOSP) isn't inherited (no `<parent>`);
  the module mixes 2- and 4-space indentation. Match the file you're editing.
- **Auth not wired**: no `SecurityFilterChain`; `/private-app/*` endpoints are open. `BadCredentialException`
  is dormant scaffolding.
- **Unit tests need no database** (pure Mockito/AssertJ): `bankaccount.service.BankAccountServiceTest`
  and `common.handler.GlobalExceptionHandlerTest`.
