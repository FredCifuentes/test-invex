# Employee Service API - Technical Assessment

REST API developed with **Spring Boot** for employee management.

---

## 📌 Overview

This project implements a complete employee management API as part of a technical assessment, including:

- CRUD operations
- Input validation
- Global exception handling
- OpenAPI/Swagger documentation
- Health check with Spring Boot Actuator
- **Dockerized execution**
- **Basic authentication with Spring Security**
- Unit tests (service layer)
- Controller tests (REST API layer)
- Clean layered architecture (Controller / Service / Repository / DTO / Mapper)

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 2.7.8**
- **Spring Web**
- **Spring Data JPA**
- **Spring Validation (Jakarta/Javax Validation)**
- **Spring Security (HTTP Basic)**
- **H2 Database**
- **Lombok**
- **Spring Boot Actuator**
- **OpenAPI / Swagger (springdoc)**
- **Docker**
- **JUnit 5**
- **Mockito**
- **MockMvc**

---

## 📁 Project Structure

```text
src/main/java/com/invex/testinvex/employee
├── config          # OpenAPI config, security config, filters, constants
├── controller      # REST endpoints
├── dto             # Request/Response DTOs
├── entity          # JPA entities
├── exception       # Custom exceptions + global exception handler
├── mapper          # Entity <-> DTO mapping
├── repository      # Spring Data JPA repositories
├── service         # Service interfaces
└── service/impl    # Service implementations
```

---

## ✅ Features Implemented

### Employee API
- **Create employee** → `POST /api/employees`
- **Create employees in batch** → `POST /api/employees/batch`
- **Get all employees** → `GET /api/employees`
- **Get employee by ID** → `GET /api/employees/{id}`
- **Search employees by name** → `GET /api/employees/search?name=...`
- **Update employee** → `PUT /api/employees/{id}`
- **Delete employee** → `DELETE /api/employees/{id}`

### Quality / Engineering Features
- DTO-based API contract (entity is not exposed directly)
- Global error handling with standardized response
- Swagger/OpenAPI documented endpoints and responses
- Validation errors and business errors handled properly
- Unit and controller test coverage
- Dockerized runtime support
- Basic Auth security for protected endpoints

---

## ✅ Input Validations

The API validates incoming requests using Bean Validation annotations.

### Examples
- Required fields (`@NotBlank`, `@NotNull`)
- Age range (`18 - 99`)
- Gender allowed values (e.g. `M`, `F`)
- Date format for `birthDate`: **`dd-MM-yyyy`**

---

## ❌ Error Handling

Errors are handled by a centralized `GlobalExceptionHandler` and return a standardized response object (e.g. `ApiErrorResponse`).

### Supported error responses
- **400 Bad Request**
  - Validation failures
  - Malformed JSON
  - Invalid date format (e.g. `1996-01-15` instead of `15-01-1996`)
- **404 Not Found**
  - Employee not found
- **500 Internal Server Error**
  - Unexpected/unhandled errors

### Example 404 Error Response
```json
{
  "timestamp": "2026-02-23T14:06:41",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "path": "/api/employees/999",
  "details": null
}
```

---

## 🔐 Security (Spring Security - Basic Auth)

This project includes **Spring Security** with **HTTP Basic Authentication**.

### Protected endpoints
All business endpoints are protected by authentication (e.g. `/api/employees/**`).

### Public endpoints (whitelisted)
The following endpoints are available without authentication for demo/monitoring purposes:

- `/swagger-ui.html`
- `/swagger-ui/**`
- `/api-docs/**`
- `/v3/api-docs/**` (if enabled by springdoc version)
- `/actuator/health`
- `/actuator/info`
- `/h2-console/**` (local development only)

### Demo credentials
```text
Username: admin
Password: admin123
```

### Postman configuration
Use **Authorization → Basic Auth** with the credentials above to test protected endpoints.

---

## 📘 API Documentation (Swagger / OpenAPI)

The project includes Swagger UI and OpenAPI JSON.

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/api-docs`

### Notes
- Success responses (`200`, `201`) are documented with `EmployeeResponseDto`
- Error responses (`400`, `404`) are documented with `ApiErrorResponse`
- `204 No Content` responses are documented without body

> Swagger UI is accessible without authentication (whitelisted) for technical assessment review.

---

## ❤️ Health Check (Actuator)

Spring Boot Actuator is enabled and exposes a health endpoint.

- **Health endpoint:** `http://localhost:8080/actuator/health`

### Example response
```json
{
  "status": "UP"
}
```

Depending on configuration, it may also include component details such as:
- `db`
- `diskSpace`
- `ping`

This is the recommended way to expose health checks instead of creating a custom manual `/health` endpoint.

---

## ▶️ How to Run the Project

### Option 1: Run from IntelliJ IDEA
Run the main class:

- `TestInvexApplication`

### Option 2: Run with Maven
```bash
mvn spring-boot:run
```

The application will start on:

- `http://localhost:8080`

---

## 🐳 Dockerization

This project includes a `Dockerfile` and `.dockerignore` for containerized execution.

### Build Docker image
```bash
docker build -t employee-service:1.0 .
```

### Run Docker container
```bash
docker run -p 8080:8080 employee-service:1.0
```

### If port 8080 is already in use
```bash
docker run -p 8081:8080 employee-service:1.0
```

Then access:
- `http://localhost:8080/swagger-ui.html` (or `8081`)
- `http://localhost:8080/actuator/health` (or `8081`)

---

## 🧪 How to Run Tests

### Run all tests
```bash
mvn test
```

### Implemented test classes
- `EmployeeServiceImplTest` → unit tests with Mockito
- `EmployeeControllerTest` → controller/API tests with MockMvc

### Note about security and tests
Controller tests are configured to focus on endpoint behavior/contract validation (security filters may be disabled in tests to avoid authentication-related interference).

---

## 📌 Example Requests

### Create Employee (`POST /api/employees`)
```json
{
  "firstName": "Fredi",
  "middleName": "Daniel",
  "lastName": "Cifuentes",
  "secondLastName": "Robledo",
  "age": 29,
  "gender": "M",
  "birthDate": "15-01-1996",
  "position": "Backend Java Developer",
  "active": true
}
```

### Batch Create Employees (`POST /api/employees/batch`)
```json
[
  {
    "firstName": "Fredi",
    "middleName": "Daniel",
    "lastName": "Cifuentes",
    "secondLastName": "Robledo",
    "age": 29,
    "gender": "M",
    "birthDate": "15-01-1996",
    "position": "Backend Java Developer",
    "active": true
  },
  {
    "firstName": "Ana",
    "middleName": "Maria",
    "lastName": "Lopez",
    "secondLastName": "Hernandez",
    "age": 31,
    "gender": "F",
    "birthDate": "10-05-1994",
    "position": "QA Engineer",
    "active": true
  }
]
```

---

## 📌 Example API Responses

### Success (`GET /api/employees/1`)
```json
{
  "id": 1,
  "firstName": "Fredi",
  "middleName": "Daniel",
  "lastName": "Cifuentes",
  "secondLastName": "Robledo",
  "age": 29,
  "gender": "M",
  "birthDate": "15-01-1996",
  "position": "Backend Java Developer",
  "active": true,
  "createdAt": "2026-02-23T13:56:43"
}
```

### Validation Error (`POST /api/employees` with empty firstName)
```json
{
  "timestamp": "2026-02-23T14:07:19",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/employees",
  "details": [
    "firstName: firstName is required"
  ]
}
```

### Invalid Date Format Error (`POST /api/employees`)
```json
{
  "timestamp": "2026-02-23T14:08:02",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid date format for birthDate. Expected format: dd-MM-yyyy",
  "path": "/api/employees",
  "details": null
}
```

### Unauthorized (`GET /api/employees` without auth)
```json
{
  "timestamp": "2026-02-23T14:10:10",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/employees"
}
```

---

## ⚙️ Configuration Notes

### Typical `application.properties` includes
- H2 console
- Hibernate/JPA settings
- Swagger paths
- Actuator exposure
- Security demo credentials
- Logging levels

### Example properties (reference)
```properties
spring.application.name=employee-service

# H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always

# Security (demo)
spring.security.user.name=admin
spring.security.user.password=admin123

# Logging
logging.level.root=INFO
logging.level.com.invex.testinvex.employee=DEBUG
logging.level.org.springframework.web=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 📝 Logging

The project can include request/service logging for observability.

### Examples implemented / recommended
- Controller logs (`INFO`): request start/end
- Service logs (`DEBUG`): method execution details
- Request logging filter (`OncePerRequestFilter`) for:
  - HTTP method
  - path
  - selected headers (safe headers only)
  - response status
  - execution time

### Security note
Avoid logging sensitive headers such as:
- `Authorization`
- `Cookie`
- tokens / credentials

### Example logging style
- `START Controller.getAll - GET /api/employees`
- `END Controller.getAll - 10 employees returned in 12 ms`
- `START Service.findAll`
- `END Service.findAll - 10 records processed in 5 ms`

---

## 🧹 Clean Code / Architecture Practices Applied

- Layered architecture (`controller`, `service`, `repository`, `dto`, `mapper`)
- Centralized exception handling (`@ControllerAdvice`)
- DTO-based API contracts (do not expose entities directly)
- Meaningful method names
- Unit tests and controller tests
- Functional style where appropriate (`stream`, `Optional`)
- Swagger contracts aligned with actual response models

---

## 🔄 Git Flow (Recommended)

Suggested branching strategy for collaborative development:

- `main` → stable / production-ready branch
- `develop` → integration branch
- `feature/*` → new features
- `hotfix/*` → urgent production fixes

### Example feature branches
- `feature/crud-employees`
- `feature/dto-mapper-validation`
- `feature/openapi-documentation`
- `feature/unit-tests`
- `feature/dockerization`
- `feature/basic-security`
- `hotfix/date-parse-handler`

---

## 📂 Evidence of Functionality

Project evidence can be included in the `evidence/` folder:

- `evidence/screenshots/` → Docker and security screenshots
- `evidence/postman/` → Postman collection export
- `evidence/video/` → demo video file

---

## 👤 Author

**Daniel Cifuentes**
