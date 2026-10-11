# Design: Migrate `drools-spring-app-v3` into `com.springboot.4.project`

- **Date:** 2026-10-11
- **Status:** Approved
- **Source:** `/home/duc/study/com.springboot.3.project/drools-spring-app-v3` (left untouched)
- **Target:** new module `drools-spring-app/` in this repo
- **Branch:** `feature/migrate-drools-spring-app` (cut from `refactor/clean-code-package-by-feature`)

## Goal

Port the Drools business-rule service (Spring Boot 3.4.4 / Java 17 / Drools 8.44 / MongoDB) to this
repo's stack and conventions: Java 25, Spring Boot 4.0.3, Jackson 3, Drools 10.2.0, **OpenAPI-first**
with **typed** request/response schemas, and **package-by-feature**.

Success criteria:
1. `mvn -pl drools-spring-app -am clean verify` compiles (both OpenAPI executions) and all tests pass
   without a running MongoDB.
2. The example rule (`example/complex-rule-with-utility-classes.json`) executed through the service
   produces `eligibleForDiscount=true`, `conflict="Missing order data"`, `youngUserBonus=false`
   (while `highValueOrder` is not set, because `order.items` is null).
3. The three v3 endpoints exist at the same paths with typed contracts.

## Source analysis (v3)

| Class | Role |
|---|---|
| `BusinessRuleController` | `POST/GET /v1/drools/business-rules`, `POST /v1/drools/business-rules/actions/test`; raw `String` bodies |
| `BusinessRuleService` | create: parses body as `org.bson.Document`, stores whole doc as `value`; test: `{data, businessRule}` → DRL → stateless session → `ResultObject` JSON |
| `DroolsService` | `JsonNode` → `PackageDescr` → DRL via `DrlDumper`; `KieHelper` → `StatelessKieSession` |
| `BusinessRule` (Mongo `business_rules`) | `UUID id`, `@Indexed(unique) name`, `Document value` |
| `GenericObject`, `ResultObject` | Rule facts / global (referenced by FQN from rule JSON `imports`) |
| `DateTimeHelper`, `JsonNodeHelper` | Static helpers callable from rule conditions |
| `MapperConfig` | Replaces Boot's `ObjectMapper` with a bare one |
| `validation/BusinessRuleValidator.json` + `everit-json-schema` | Unused |

## 1. Module & build

- New module `drools-spring-app/`, registered in root `pom.xml` `<modules>`.
- Pom shaped like `functional-programming`: **no `<parent>`**, imports `spring-boot-dependencies` 4.0.3
  BOM, Java 25, annotation processors MapStruct → Lombok → lombok-mapstruct-binding →
  spring-boot-configuration-processor.
- Dependencies: `spring-boot-starter-web` (as in `functional-programming`), `spring-boot-starter-data-mongodb`,
  `spring-boot-starter-validation`, `spring-boot-starter-actuator`; Drools 10.2.0 (`drools-core`,
  `drools-compiler`, `drools-mvel`, `kie-api`, `kie-internal`); MapStruct, Lombok, JSpecify,
  swagger-annotations, springdoc; test: `spring-boot-starter-test`.
- **Dropped (unused):** `everit-json-schema` + `BusinessRuleValidator.json`, `commons-lang3`,
  `slf4j-simple`, explicit `hibernate-validator 9.0.0.CR1` (managed by the starter).
- **`MapperConfig` dropped:** it replaces Boot's auto-configured mapper with a bare `new ObjectMapper()`;
  its settings are Jackson 3 defaults or unnecessary once bodies are typed.
- OpenAPI Generator 7.20.0, **two executions**, same pattern as `functional-programming`:
  - `common.yaml` → models only → `com.springboot.project.common.generated.dto` (`ErrorResponse`).
  - `business-rule.yaml` → `com.springboot.project.businessrule.generated.{api,dto}`; shared
    `ErrorResponse` referenced via `schemaMappings` + `importMappings` (generated once, imported).
  - `modelNameSuffix=dto`, `interfaceOnly`, `useSpringBoot4`, `useJackson3`, `useBeanValidation`,
    `openApiNullable=false`.

## 2. API contract (`business-rule.yaml`)

Paths unchanged from v3:

| Method & path | Request → Response |
|---|---|
| `POST /v1/drools/business-rules` | `CreateBusinessRuleRequest {businessRule}` → **201** `BusinessRuleResponse {id, name, definition}` |
| `GET /v1/drools/business-rules` | → **200** `BusinessRuleResponse[]` |
| `POST /v1/drools/business-rules/actions/test` | `TestBusinessRuleRequest {data: object, businessRule}` → **200** `TestBusinessRuleResponse {result: object}` |

Schemas (`*` = required):

```
BusinessRuleDefinition { name*, imports[string], globals[Global], rules*[Rule] }
Global                 { type*, identifier* }
Rule                   { rule*, when*[Pattern], then*[Consequence], conflicts[Conflict] }
Pattern                { identifier*, type*, conditions*[string] }
Consequence            { actions*[string] }
Conflict               { name*, identifier*, type*, conditions*[string], actions*[string], conflictDescription }
```

`data` and `result` are free-form JSON objects (`type: object`). Bean validation rejects malformed
bodies with 400. Error responses use `ErrorResponse`.

**Behaviour fix:** v3 built each conflict rule's pattern from the *first `when` pattern's*
`identifier`/`type`, ignoring the conflict's own fields. The new builder uses the conflict's own
`identifier`/`type` (identical in the example).

## 3. Packages & data flow (`com.springboot.project`)

```
┌──────────────────────┐   ┌──────────────────────────┐   ┌──────────────────────────┐
│ BusinessRuleController│──▶│ IBusinessRuleService      │──▶│ BusinessRuleRepository   │──▶ Mongo
│ implements generated  │   │ BusinessRuleService       │   │ (MongoRepository)        │  business_rules
│ BusinessRulesApi      │   │                           │   └──────────────────────────┘
└──────────────────────┘   │  test(): DrlBuilder ──────▶ RuleEngine (KieHelper,       │
     Dto ⇄ Model           │          (Model → DRL)      stateless session, execute) │
     (MapStruct MAPPER)     └──────────────────────────┘
```

```
DroolsSpringApp                      (root package → scan covers all features)
businessrule/
  controller/BusinessRuleController  implements generated *Api, delegates to IBusinessRuleService
  service/IBusinessRuleService, BusinessRuleService
  repository/BusinessRuleRepository  MongoRepository<BusinessRuleEntity, UUID> (no port/adapter: one repo)
  entity/BusinessRuleEntity          @Document("business_rules"): UUID id, @Indexed(unique) name,
                                     BusinessRuleDefinitionModel definition (nested fields)
  model/                             records: BusinessRuleDefinitionModel, GlobalModel, RuleModel,
                                     PatternModel, ConsequenceModel, ConflictModel
  mapper/BusinessRuleDtoMapper       MapStruct, static MAPPER (Dto ⇄ Model)
  drl/DrlBuilder                     pure: BusinessRuleDefinitionModel → DRL string (PackageDescr + DrlDumper)
  engine/RuleEngine                  newSession(drl, packageName): KieHelper; verify() first → compile
                                     errors become BadRequestException (400); returns StatelessKieSession
                                     (service sets global "result" and executes the facts)
  fact/GenericObject, ResultObject   rule-facing facts/global
common/
  exception/BadRequestException
  handler/GlobalExceptionHandler     @RestControllerAdvice extending ResponseEntityExceptionHandler:
                                     400 BadRequestException / 500 other Exception → ErrorResponseDto;
                                     bean-validation + unreadable body → 400 ProblemDetail (inherited)
  util/DateTimeHelper, JsonNodeHelper  unchanged apart from Jackson 3
```

Test-endpoint flow: `data` (`Map`) → `JsonMapper.valueToTree` → `GenericObject`; `ResultObject.result`
= deep copy of `data`; `DrlBuilder` → DRL; `RuleEngine` compiles + executes with facts
`[GenericObject, ResultObject]` and global `result`; response `result` = `ResultObject.result`.

**Jackson 3 port:** `GenericObject`/`ResultObject`/`JsonNodeHelper` use `tools.jackson.databind`
types; `isTextual()/asText()` → `isString()/asString()`.

**Example rule** moves to the new module with imports rewritten to the new FQNs
(`com.springboot.project.businessrule.fact.*`, `com.springboot.project.common.util.*`); the
`com.fasterxml.jackson.databind.JsonNode` import is removed (no longer on the classpath, unused).

## 4. Config, compatibility, tests, docs

- `application.yaml`: Boot 4 key `spring.mongodb.uri` (same URI as v3),
  `uuid-representation: standard`, graceful shutdown timeout kept; port stays default 8080.
- **No v3 data compatibility:** stored shape changes (`value` → `definition`) and UUID representation
  is pinned to `standard`; v3 documents are not migrated. Rule JSON must use the new import FQNs.
- **Out of scope (unchanged from v3):** the unique index on `name` is not actually created (Boot
  `auto-index-creation` is off); no 409 on duplicate name; no auth.
- **Tests (no MongoDB required):**
  - `DrlBuilderTest` — DRL output for package/imports/globals/rules, and the conflict-pattern fix.
  - `RuleEngineTest` — invalid DRL → `BadRequestException`.
  - `BusinessRuleServiceTest` — real Drools engine on the example rule with a mocked repository;
    asserts the success-criteria results; create maps and saves the definition.
  - `GlobalExceptionHandlerTest` — status + `ErrorResponseDto` body per exception type.
  - `BusinessRuleControllerTest` — standalone MockMvc: handled errors are written as a JSON body
    (added after finding `@ControllerAdvice` turned them into 404 view forwards).
  - `BusinessRuleEntityMappingTest` — Mongo converter round-trip of the entity with nested records.
- **Docs:** module `drools-spring-app/CLAUDE.md` in the sibling style; root `AGENTS.md` module list
  updated.
