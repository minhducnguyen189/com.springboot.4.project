# CLAUDE.md — drools-spring-app

Module-scoped guidance. The repo-root `../CLAUDE.md` behavioral rules also apply; `../AGENTS.md`
holds repo-wide context. Design and plan: `../docs/2026-10-11-drools-spring-app-migration-*.md`.

## What this module is
Spring Boot 4 port of `com.springboot.3.project/drools-spring-app-v3`: business rules are defined as
**typed JSON**, rendered to **DRL**, compiled with **Drools 10.2.0** and executed against free-form
input data. Rule definitions are stored in **MongoDB** (`business_rules`). OpenAPI-first,
package-by-feature.

## Stack
Java 25 · Maven 3.9.12 · Spring Boot 4.0.3 (web, data-mongodb, validation, actuator) · **Jackson 3**
(`tools.jackson.*`) · Drools 10.2.0 (`KieHelper`, `DrlDumper`) · OpenAPI Generator 7.20.0 (2 executions)
· MapStruct 1.6.3 · Lombok · JUnit 5 + Mockito + AssertJ.

## Build / Run / Test — run from the **repo root**
No `<parent>` (imports the Spring Boot BOM), so root Spotless/AOSP is **not inherited**; files use
4-space indentation.
```bash
mvn -pl drools-spring-app test               # unit + ArchUnit tests (*Test, Surefire) — no DB/containers
mvn -pl drools-spring-app clean verify       # + Testcontainers integration tests (*IT, Failsafe)
cd drools-spring-app && mvn spring-boot:run  # port 8080; Mongo needed only for create/get
```
Mongo URI (hardcoded in `application.yaml`): `mongodb://root:example@localhost:27017/admin`.
Without Mongo the app still starts and `/actions/test` works; `/actuator/health` reports DOWN.

## Endpoints (`src/main/resources/openapi/business-rule.yaml`)
| Method & path | Body → Response |
|---|---|
| `POST /v1/drools/business-rules` | `{businessRule}` → 201 `{id, name, definition}` |
| `GET /v1/drools/business-rules` | → 200 `[{id, name, definition}]` |
| `POST /v1/drools/business-rules/actions/test` | `{data, businessRule}` → 200 `{result}` |

Sample request: `src/main/resources/example/complex-rule-with-utility-classes.json` (works as-is for
the test endpoint). Bruno collection with assertions: `../bruno/drools-spring-app/` (environment
`Local`); keep its example bodies in sync with the example JSON.

## Package layout (`com.springboot.project`)
```
DroolsSpringApp
businessrule/  controller (implements generated BusinessRuleApi) · service (IBusinessRuleService)
               · repository (MongoRepository) · entity (BusinessRuleEntity) · model (records)
               · mapper (BusinessRuleDtoMapper, static MAPPER) · drl (DrlBuilder) · engine (RuleEngine)
               · fact (GenericObject, ResultObject) · generated.{api,dto}
common/        exception (BadRequestException) · handler (GlobalExceptionHandler)
               · util (DateTimeHelper, JsonNodeHelper) · generated.dto (ErrorResponseDto)
```
Test flow: controller converts `data` (`Map`) to `ObjectNode` → `DrlBuilder.build(definition)` →
`RuleEngine.newSession(drl, name)` → global `result` = `ResultObject` holding a deep copy of `data`;
facts `[GenericObject(data), resultObject]` → response is `resultObject.result`.

## Gotchas
- **Rule JSON references Java classes by FQN** (`imports`, `globals[].type`, conditions calling
  `DateTimeHelper`). Moving/renaming anything in `businessrule.fact` or `common.util` breaks stored
  rules. They are Jackson 3 types: use `tools.jackson.databind.JsonNode`, not `com.fasterxml…`.
- `GenericObject` getters **return `null`** for missing/mistyped paths on purpose — rules test
  `!= null`. Don't "fix" them to `Optional`.
- **Conflicts** compile to separate rules using the conflict's own `identifier`/`type`
  (v3 used the first `when` pattern's). `conflictDescription` is stored but not rendered.
- **Non-compiling rules → 400** with Drools messages (`RuleEngine` calls `KieHelper.verify()` first).
  Drools logs an `ERROR ... Unable to build KieBaseModel` line for these — expected.
- **`@RestControllerAdvice` is required** — with `@ControllerAdvice` the `ErrorResponseDto` is
  resolved as a view and errors become 404s. `BusinessRuleControllerTest` guards this. Bean-validation
  and unreadable-body 400s come from `ResponseEntityExceptionHandler` as `ProblemDetail`, not
  `ErrorResponseDto`.
- **Boot 4 Mongo keys** are `spring.mongodb.*`. `spring.mongodb.representation.uuid: standard` is
  required — Boot's default `unspecified` cannot encode the `UUID` id.
- **OpenAPI examples containing `"`** break the generated Java (unescaped in annotations) — keep
  rule-condition examples out of the spec.
- Unique index on `name` is declared but **not created** (Boot `auto-index-creation` is off); no 409
  on duplicates. v3 Mongo documents (`value` field, legacy UUIDs) are not readable.
- **Example rule bug (inherited from v3):** `MissingOrderFields` uses `$isNull: (...)`, which in DRL is
  a *binding*, not a constraint — the conflict fires for every order. Dropping the `$isNull:` prefix
  fixes it; `ExampleRuleScenarioTest.should_not_record_conflict_when_order_fields_are_present` is
  `@Disabled` until the example JSON is fixed.

## Tests
- **Unit (`*Test`, Surefire, no DB):** `ExampleRuleFixture` loads the example JSON (fresh copies per
  call). `ExampleRuleScenarioTest` runs the example's real rules through Drools, varying one input
  per case; `ExampleRuleDrlTest` (5 rules render + compile); `DrlBuilderTest`, `RuleEngineTest`,
  `BusinessRuleServiceTest`, `GenericObjectTest`, `DateTimeHelperTest`, `BusinessRuleDtoMapperTest`,
  `BusinessRuleControllerTest` (standalone MockMvc), `BusinessRuleEntityMappingTest`,
  `GlobalExceptionHandlerTest`.
- **Structure:** `ArchitectureTest` (ArchUnit) — controllers use service interfaces only, repositories
  only from services, generated `*Dto`/`*Api` stay in controller/mapper/handler, `common` ↛
  `businessrule`, `fact`/`util` Spring-free, models are records, no slice cycles, no `System.out`.
- **Integration (`*IT`, Failsafe in `verify`):** `BusinessRuleRepositoryIT` (`@DataMongoTest`) and
  `BusinessRuleApiIT` (`RANDOM_PORT` + `RestTestClient`) against `MongoContainerConfiguration`
  (`@ServiceConnection`). `@Testcontainers(disabledWithoutDocker = true)` → **skipped, not failed,**
  without a container runtime.
  - Image is **`mongo:7.0`** on purpose: MongoDB 8.0+/9.0 refuse to start on Linux kernels
    6.19–7.0.13 (SERVER-121912; this machine runs 7.0.0).
  - Failsafe is configured with `classesDirectory=target/classes` — without it the repackaged Boot
    jar hides classes under `BOOT-INF/` and ITs fail with `NoClassDefFoundError`.
  - **Podman** works as the runtime: `systemctl --user enable --now podman.socket` plus
    `~/.testcontainers.properties` with
    `docker.host=unix:///run/user/<uid>/podman/podman.sock` and `ryuk.disabled=true`.
