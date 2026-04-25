# AGENTS.md — AI Coding Assistant Configuration

## Project Overview

Multi-module Spring Boot Maven project with two modules:
- `table-with-million-records` — REST API with offset/cursor pagination for large datasets
- `functional-programming` — Bank account & transaction services with functional patterns

---

## Technology Stack

| Component | Version |
|-----------|---------|
| Java | 25 |
| Maven | 3.9.12 |
| Spring Boot | 4.0.3 |
| PostgreSQL | 42.7.10 |
| MapStruct | 1.6.3 |
| Lombok | 1.18.42 |
| Spotless | 2.44.4 |

---

## Build Commands

```bash
mvn clean compile          # Compile + Spotless check
mvn test                   # Run tests
mvn spotless:apply         # Apply formatting
mvn clean package -DskipTests   # Package without tests
```

---

## Code Conventions

- **Formatting**: Google Java Format (AOSP) via Spotless — enforced on every commit
- **Mappers**: Use `@Mapper` with static `MAPPER` field — never inject as Spring beans
- **Dependencies**: Constructor injection (`@Autowired`) for repositories only
- **Test naming**: snake_case with pattern `should_<action>_when_<condition>`
- **Generated code**: Do NOT edit `target/generated-sources/` — regenerate via `mvn compile`

---

## Key Architecture Patterns

1. **OpenAPI-first**: Define contracts in `open-api.yaml`, generate interfaces, controllers implement them
2. **JPA Specifications + QBE**: Dynamic filtering via Query by Example + Specification API
3. **Pagination**: Both offset-based (`PageRequest`) and cursor-based (`sequenceNumber`)
4. **Exception handling**: `@ControllerAdvice` → standardized `ErrorResponseModel`

---

## Important Notes

- First run seeds **10M transaction rows** + **200K bank accounts** — takes several minutes
- Application runs on **port 7070**
- Annotation processor order: **MapStruct → Lombok → lombok-mapstruct-binding → spring-boot-configuration-processor**