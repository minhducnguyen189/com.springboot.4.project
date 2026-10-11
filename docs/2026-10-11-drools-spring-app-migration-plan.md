# drools-spring-app Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan
> task-by-task. Steps use checkbox (`- [x]`) syntax for tracking.

**Goal:** Add a `drools-spring-app` module that ports `drools-spring-app-v3` to Java 25 / Spring Boot
4.0.3 / Jackson 3 / Drools 10.2.0, OpenAPI-first with typed schemas, package-by-feature.

**Architecture:** Generated `BusinessRulesApi` → `BusinessRuleController` → `IBusinessRuleService` →
(`BusinessRuleRepository` for CRUD | `DrlBuilder` + `RuleEngine` for test execution). Dto ⇄ Model via a
static MapStruct mapper; Mongo entity embeds the definition model records.

**Tech Stack:** Java 25, Maven 3.9.12, Spring Boot 4.0.3 (webmvc, data-mongodb, validation,
actuator), Jackson 3, Drools 10.2.0, OpenAPI Generator 7.20.0, MapStruct 1.6.3, Lombok 1.18.42,
JUnit 5 + Mockito + AssertJ.

**Spec:** `docs/2026-10-11-drools-spring-app-migration-design.md`

## Global Constraints

- Module has no `<parent>`; imports `spring-boot-dependencies` 4.0.3 BOM; Java 25.
- Base package `com.springboot.project`; entrypoint `DroolsSpringApp` at the root package.
- Endpoint paths unchanged: `/v1/drools/business-rules`, `/v1/drools/business-rules/actions/test`.
- Mappers: MapStruct with static `MAPPER`, never injected.
- Tests named `should_<action>_when_<condition>`; no MongoDB needed for `mvn verify`.
- Never edit `target/generated-sources/`.
- Commits: `Type: Subject` (no issue number exists for this work).

---

### Task 1: Module scaffold + OpenAPI contract compiles

**Files:**
- Create: `drools-spring-app/pom.xml`
- Create: `drools-spring-app/src/main/resources/openapi/common.yaml`, `.../openapi/business-rule.yaml`
- Create: `drools-spring-app/src/main/resources/application.yaml`
- Create: `drools-spring-app/src/main/java/com/springboot/project/DroolsSpringApp.java`
- Modify: `pom.xml` (root) — add `<module>drools-spring-app</module>`

**Produces:** generated `businessrule.generated.api.BusinessRulesApi` and DTOs
`CreateBusinessRuleRequestDto`, `TestBusinessRuleRequestDto`, `TestBusinessRuleResponseDto`,
`BusinessRuleResponseDto`, `BusinessRuleDefinitionDto`, `GlobalDto`, `RuleDto`, `PatternDto`,
`ConsequenceDto`, `ConflictDto`; `common.generated.dto.ErrorResponseDto`.

- [x] Write pom, specs, app class, yaml.
- [x] Run `mvn -pl drools-spring-app -am clean compile` → BUILD SUCCESS; inspect generated signatures.
- [x] Commit `Build: Scaffold drools-spring-app module with OpenAPI contract`.

### Task 2: Facts, helpers, models, mapper (Jackson 3 port)

**Files:**
- Create: `businessrule/fact/GenericObject.java`, `businessrule/fact/ResultObject.java`
- Create: `common/util/DateTimeHelper.java`, `common/util/JsonNodeHelper.java`
- Create: `businessrule/model/{BusinessRuleDefinitionModel,GlobalModel,RuleModel,PatternModel,ConsequenceModel,ConflictModel}.java` (records)
- Create: `businessrule/mapper/BusinessRuleDtoMapper.java`
- Create: `common/exception/BadRequestException.java`

**Produces:** `record BusinessRuleDefinitionModel(String name, List<String> imports, List<GlobalModel> globals, List<RuleModel> rules)`,
`record GlobalModel(String type, String identifier)`,
`record RuleModel(String rule, List<PatternModel> when, List<ConsequenceModel> then, List<ConflictModel> conflicts)`,
`record PatternModel(String identifier, String type, List<String> conditions)`,
`record ConsequenceModel(List<String> actions)`,
`record ConflictModel(String name, String identifier, String type, List<String> conditions, List<String> actions, String conflictDescription)`;
`BusinessRuleDtoMapper.MAPPER.toModel(BusinessRuleDefinitionDto)`, `.toDto(BusinessRuleDefinitionModel)`,
`.toResponseDto(BusinessRuleEntity)`.

- [x] Port classes; compile → BUILD SUCCESS.

### Task 3: DrlBuilder (TDD)

**Files:** Create `businessrule/drl/DrlBuilder.java`; Test `businessrule/drl/DrlBuilderTest.java`

**Produces:** `@Component DrlBuilder#build(BusinessRuleDefinitionModel) : String`

- [x] Tests: `should_render_package_imports_globals_and_rules_when_building_drl` (asserts `package X`,
  `import ...`, `global T result`, `rule "R"`, constraint text, consequence `a;b;`);
  `should_use_conflict_own_pattern_when_building_conflict_rule` (conflict identifier/type differ from
  when → DRL conflict rule uses conflict's); `should_skip_conflicts_when_rule_has_none`.
- [x] Run → FAIL (class missing); implement; run → PASS; commit.

### Task 4: RuleEngine (TDD)

**Files:** Create `businessrule/engine/RuleEngine.java`; Test `businessrule/engine/RuleEngineTest.java`

**Produces:** `@Component RuleEngine#newSession(String drl, String packageName) : StatelessKieSession`
— throws `BadRequestException` listing Drools error messages when DRL does not compile.

- [x] Tests: `should_throw_bad_request_when_drl_does_not_compile`; `should_fire_rule_and_update_global_when_drl_is_valid`.
- [x] FAIL → implement → PASS → commit.

### Task 5: Entity, repository, service (TDD)

**Files:** Create `businessrule/entity/BusinessRuleEntity.java`, `businessrule/repository/BusinessRuleRepository.java`,
`businessrule/service/IBusinessRuleService.java`, `businessrule/service/BusinessRuleService.java`;
Test `businessrule/service/BusinessRuleServiceTest.java`; Move `example/complex-rule-with-utility-classes.json`
(imports rewritten) into `src/main/resources/example/`.

**Produces:** `IBusinessRuleService#createBusinessRule(BusinessRuleDefinitionModel) : BusinessRuleEntity`,
`#getBusinessRules() : List<BusinessRuleEntity>`,
`#testBusinessRule(ObjectNode data, BusinessRuleDefinitionModel definition) : ObjectNode`.

- [x] Tests (real `DrlBuilder` + `RuleEngine`, mocked repo): example rule → `eligibleForDiscount=true`,
  `conflict="Missing order data"`, `youngUserBonus=false`, no `highValueOrder`;
  create → saves entity with random id, name = definition name.
- [x] FAIL → implement → PASS → commit.

### Task 6: Controller + GlobalExceptionHandler (TDD)

**Files:** Create `businessrule/controller/BusinessRuleController.java`, `common/handler/GlobalExceptionHandler.java`;
Test `common/handler/GlobalExceptionHandlerTest.java`

- [x] Tests: BadRequestException → 400 body; generic Exception → 500 body; standalone MockMvc
  controller test (400 JSON body over HTTP); Mongo converter round-trip of the entity.
- [x] FAIL → implement → PASS; `mvn -pl drools-spring-app -am clean verify` → all green; commit.

### Task 7: Docs

**Files:** Create `drools-spring-app/CLAUDE.md`; Modify `AGENTS.md` (module list).

- [x] Write docs; commit `Docs: Document drools-spring-app module`.
