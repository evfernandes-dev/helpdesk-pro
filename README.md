# HelpDesk Pro

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A robust, production-ready REST API for technical support ticket management, built with modern Java 21, Spring Boot 3.4, and MySQL.

---

## Features

- **Full CRUD Operations**: Create, list, search by ID or status, update, and delete support tickets (`chamados`).
- **Automated Lifecycle Management**: Closing dates (`dataFechamento`) are automatically populated upon ticket closure and cleared upon reopening.
- **Data Validation**: Bean Validation constraints (`@NotBlank`, `@Size`) ensuring data integrity at the API boundary.
- **Exception Handling**: Standardized error handling returning clean HTTP status codes (e.g., `404 Not Found` for missing resources).
- **Interactive API Documentation**: Swagger UI integrated via `springdoc-openapi` (`/swagger-ui.html`).
- **Responsive Web Frontend**: Clean static dashboard (`index.html`) serving as a client interface.
- **Containerized Environment**: Fully containerized with Docker and Docker Compose including health checks and persistent storage.
- **Automated Testing**: Comprehensive unit tests with JUnit 5 and Mockito verifying service business logic.

---

## Tech Stack

- **Core & Language**: Java 21 (LTS), Spring Boot 3.4.5
- **Persistence**: Spring Data JPA, Hibernate, MySQL 8.0
- **Validation & Docs**: Jakarta Bean Validation, Springdoc OpenAPI 2.8.5 (Swagger UI)
- **Build Tool**: Apache Maven
- **Testing**: JUnit 5, Mockito
- **Containerization**: Docker, Docker Compose

---

## Architecture & Project Structure

HelpDesk Pro follows a classic, clean **layered architecture**:
1. **Controller Layer (`controller`)**: Exposes REST endpoints, handling HTTP requests and responses.
2. **Service Layer (`service`)**: Encapsulates core business logic and transactional boundaries (`@Transactional`).
3. **Repository Layer (`repository`)**: Spring Data JPA interfaces communicating with the relational database.

```text
helpdesk-pro/
├── src/
│   ├── main/
│   │   ├── java/com/example/helpdesk/
│   │   │   ├── controller/      # REST Controllers (ChamadoController)
│   │   │   ├── exception/       # Global & Custom Exceptions (RecursoNaoEncontradoException)
│   │   │   ├── model/           # JPA Entities & Enums (Chamado, StatusChamado)
│   │   │   ├── repository/      # Spring Data JPA Repositories (ChamadoRepository)
│   │   │   └── service/         # Business Logic Services (ChamadoService)
│   │   └── resources/
│   │       ├── static/          # Web Frontend (index.html)
│   │       └── application.properties # Configuration & environment bindings
│   └── test/
│       └── java/com/example/helpdesk/
│           └── service/         # Unit Tests (ChamadoServiceTest)
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Domain & Business Rules

The system manages support tickets (`Chamados`) with the following domain attributes and rules:

- **Status Values**:
  - `ABERTO` (Open)
  - `EM_ANDAMENTO` (In Progress)
  - `FECHADO` (Closed)
  *(Note: Portuguese domain terms and status values are preserved to reflect the core business domain model).*
- **Business Rules**:
  - New tickets default to status `ABERTO` and record `dataCriacao` (creation timestamp).
  - When a ticket status transitions to `FECHADO`, `dataFechamento` (closing timestamp) is automatically populated with the current timestamp.
  - If a closed ticket is reopened (status changed to `ABERTO` or `EM_ANDAMENTO`), `dataFechamento` is automatically cleared (`null`).
  - Read operations automatically sort tickets in descending order by creation date (`findAllByOrderByDataCriacaoDesc`).

---

## API Endpoints

Base URL: `http://localhost:8080`

| Method | Route | Description |
| :--- | :--- | :--- |
| `GET` | `/chamados` | List all support tickets (sorted by newest first) |
| `GET` | `/chamados/{id}` | Get ticket details by ID |
| `GET` | `/chamados/status/{status}` | Filter tickets by status (`ABERTO`, `EM_ANDAMENTO`, `FECHADO`) |
| `POST` | `/chamados` | Create a new support ticket |
| `PUT` | `/chamados/{id}` | Update an existing ticket |
| `DELETE` | `/chamados/{id}` | Delete a ticket by ID |

### Request & Response Examples

#### Create Ticket (`POST /chamados`)
**Request Body**:
```json
{
  "titulo": "Erro ao acessar o sistema",
  "descricao": "O usuário recebe erro 500 ao tentar fazer login."
}
```

**Response (`201 Created`)**:
```json
{
  "id": 1,
  "titulo": "Erro ao acessar o sistema",
  "descricao": "O usuário recebe erro 500 ao tentar fazer login.",
  "status": "ABERTO",
  "dataCriacao": "2026-10-24T15:00:00",
  "dataFechamento": null
}
```

---

## Interactive Documentation (Swagger UI)

When the application is running, access the interactive OpenAPI / Swagger documentation at:
- **URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## How to Run

### Option 1: With Docker Compose (Recommended)
Make sure Docker and Docker Compose are installed on your machine.

1. Clone the repository and navigate to the project directory:
   ```bash
   git clone https://github.com/evfernandes-dev/helpdesk-pro.git
   cd helpdesk-pro
   ```
2. Start containers in detached mode:
   ```bash
   docker compose up --build -d
   ```
3. The application will be running at `http://localhost:8080` and MySQL at port `3306`.
4. To stop the containers:
   ```bash
   docker compose down
   ```

### Option 2: Locally with Maven and MySQL
1. Ensure Java 21 and MySQL are installed and running locally.
2. Create a MySQL database named `helpdesk_pro`:
   ```sql
   CREATE DATABASE helpdesk_pro;
   ```
3. Configure your database credentials in `src/main/resources/application.properties` or set environment variables:
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/helpdesk_pro?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Sao_Paulo
   export SPRING_DATASOURCE_USERNAME=root
   export SPRING_DATASOURCE_PASSWORD=your_password
   ```
4. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

---

## Running Tests

The test suite covers core business logic, status transitions, validation, and exception handling using JUnit 5 and Mockito.

To run the tests:
```bash
mvn test
```

---

## Screenshots

| Ticket List Dashboard | Ticket Management Form |
| :---: | :---: |
| ![Ticket List](docs/img/ticket-list.png) | ![Ticket Form](docs/img/ticket-form.png) |

*(Note: To populate these screenshots on GitHub, capture UI snapshots of the running web interface and save them as `docs/img/ticket-list.png` and `docs/img/ticket-form.png`).*

---

## Future Improvements

- [ ] **Spring Security & JWT Authentication**: Add user roles (Admin, Technician, Customer) and secured endpoints.
- [ ] **Pagination & Sorting**: Implement `Pageable` on listing endpoints for large datasets.
- [ ] **Advanced Filtering**: Date range filters and technician assignment tracking.
- [ ] **CI/CD Pipeline**: GitHub Actions workflow for automated testing and container builds.

---

## Author & License

Developed by **Emanuel Vitor** ([@evfernandes-dev](https://github.com/evfernandes-dev)).

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
