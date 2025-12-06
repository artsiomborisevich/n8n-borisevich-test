# CLAUDE.md

## Project Overview
Project Name: AI Agent
Goals:
- Provide real-time processing of customer requests
- Automate monitoring and alerting for operational anomalies
- Maintain high availability and scalable architecture

Mission: Enable seamless development workflow and DevOps automation with CLAUDE Code, minimizing manual intervention while ensuring code quality and security.

---

## Tech Stack
Backend:
- Java 21 (feature flags toggled per environment)
- Spring Boot 3.x, Spring Data, Spring Cloud (Feign, Circuit Breakers)
- MapStruct for DTO mapping, Lombok for boilerplate reduction (Do not use Lombok with records)
- Use interface with postfix **UseCase** when data is updated and **Query** when only retrieved
- Use MapStruct for mapping

Frontend:
- React 18, TypeScript 5.x, TailwindCSS

Infrastructure:
- Kubernetes 1.28+, Helm 3.x, Terraform 1.5.x
- Prometheus, Grafana for metrics and alerting
- Postfix for internal mail simulation in QA

CI/CD:
- Jenkins pipelines (Groovy scripted), GitHub Actions for code quality
- Docker 24+, Docker Compose for local testing
- ECR for container registry, ArgoCD for deployment

Databases:
- MongoDB 6.x for document storage

Monitoring & Logging:
- Micrometer + Prometheus

Testing:
- AssertJ used for testing instead of JUnit
- Use comments //Given //When //Then in tests
- Name of the test methods #{methodUnderTest}_when{ConditionInCamelCase}_should{ExpectedResultInCamelCase}#
- Prefer to use separate creation method for test data. Don't create it in @BeforeEach
- Don't use @DisplayName in tests

---

## Architectural Patterns

### Package Structure
```
net.idt
└── order
    └── adapter
        └── in
            └── web
                └── CreateOrderRequest
                └── CreateOrderResponse
└── customer
    ├── adapter
    │   ├── mongo
    │   │   ├── CustomerMongoAdapter
    │   │   ├── entity
    │   │   │   └── CustomerDocument
    │   │   └── mapper
    │   │       └── CustomerDocumentMapper
    │   └── oracle
    │       └── entity
    │           └── CustomerEntity
    ├── domain
    │   └── Customer
    └── application
        ├── service
            └── CustomerActivationService
        ├── port
        │   ├── out
        │   │   ├── model
        │   │   │   ├── CustomerActivationPayload
        │   │   │   └── CustomerActivationOutcome
        │   │   └── CustomerActivationPort
        │   └── in
        │       ├── CustomerActivationUseCase
        │       ├── GetCustomerDetailsQuery
        │       └── model
        │           ├── CustomerActivationCommand
        │           └── CustomerActivationResult
└── shared
    ├── http
    │   ├── ProviderClient
    │   ├── ProviderClientProperties
    │   └── ProviderRetryHandler
    ├── contract
    │   ├── *RequestData
    │   ├── *ResponseData
    │   └── *RequestParams
    └── util
        └── configuration
            └── GlobalServiceMongoConfiguration
└── exception
    ├── ExceptionController
    └── common
        └── EntityNotFoundException
```

### Services
- Implement business logic in service classes

---

## Standards
Code Guidelines:
- ALWAYS use @Transactional only where necessary
- NEVER use raw JDBC; prefer Spring Data Repositories
- MUST follow naming conventions: camelCase for variables, PascalCase for classes
- ALWAYS write unit tests with JUnit5 + AssertJ, using Given-When-Then style
- NEVER push code that breaks CI pipeline

Commits & Branching:
- ALWAYS prefix commits with scope: [service], [infra], [docs]
- NEVER commit secrets or environment configs
- MUST create feature branches from dev or main based on workflow

Documentation:
- ALWAYS update CLAUDE.md and Confluence ADRs for new agents or processes
- NEVER skip code documentation in public-facing modules
- MUST provide usage examples for scripts in README or internal wiki

---

## Security Rules
Critical Security Requirements:
- NEVER log sensitive fields (card numbers, CVV, passwords)
- MUST use RBAC in Kubernetes, limit service account privileges
- ALWAYS rotate secrets every 90 days or less
- NEVER commit .env, .key, or other secret files
- MUST validate all input in services exposed externally
- ALWAYS scan Docker images for CVEs before deployment

Authentication & Authorization:
- MUST use OAuth2 / JWT with short-lived tokens
- NEVER allow admin privileges by default

---

## Common Commands
Docker:

docker build -t <service_name> .
docker run -d -p 8080:8080 <service_name>
docker ps -a
docker logs <container_id>

Kubernetes / Helm:

kubectl get pods -n qa
kubectl describe pod <pod_name> -n qa
helm upgrade --install postfix ./helm-charts/postfix -n qa --values ./helm-charts/postfix/values.yaml
kubectl apply -f k8s/deployment.yaml

Testing:
./gradlew test
./gradlew jacocoTestReportclaude --version

## Testing Requirements
- ALWAYS cover service methods that handle business logic
- MUST write unit tests for edge cases
- NEVER skip integration tests for Feign clients and database queries
- Coverage target: 80% minimum per module
