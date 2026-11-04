# HelpDesk Pro

A REST API for support ticket management, built as a study and portfolio project with **Java 21**, **Spring Boot 3.4**, **Spring Data JPA** and **MySQL**.

The application exposes CRUD endpoints for tickets, applies a simple status lifecycle, validates request data, and includes a small static dashboard for manual testing.

> **Project status:** study/portfolio project. It is not production-ready and does not include authentication yet.

---

## Features

- Create, list, search by ID or status, update and delete tickets (`chamados`).
- New tickets start with status `ABERTO` and a creation timestamp.
- When a ticket is closed, `dataFechamento` is filled automatically; when it is reopened, the field is cleared.
- Bean Validation constraints (`@NotBlank`, `@Size`) on title and description.
- `GET /chamados/{id}` returns `404 Not Found` when the ticket does not exist.
- Swagger UI generated with Springdoc OpenAPI.
- Simple static dashboard served from `src/main/resources/static`.
- Docker Compose setup with a MySQL health check and a persistent volume.
- Unit tests for the service layer with JUnit 5 and Mockito.

---

## Tech stack

- **Language and framework:** Java 21, Spring Boot 3.4.5
- **Persistence:** Spring Data JPA, Hibernate, MySQL 8.0
- **Validation:** Jakarta Bean Validation
- **API documentation:** Springdoc OpenAPI 2.8.5 (Swagger UI)
- **Build:** Maven
- **Tests:** JUnit 5, Mockito
- **Containers:** Docker, Docker Compose

---

## Architecture

The project uses a layered structure:

1. **Controller** (`controller`): exposes the REST endpoints.
2. **Service** (`service`): business rules and transaction boundaries.
3. **Repository** (`repository`): Spring Data JPA interfaces.
4. **Model** (`model`): JPA entities and the status enum.
5. **Exception** (`exception`): custom exception for missing resources.

```text
helpdesk-pro/
├── src/
│   ├── main/
│   │   ├── java/com/example/helpdesk/
│   │   │   ├── controller/      # REST controller
│   │   │   ├── exception/       # Custom exception
│   │   │   ├── model/           # Entity and status enum
│   │   │   ├── repository/      # Spring Data JPA repository
│   │   │   └── service/         # Business logic
│   │   └── resources/
│   │       ├── static/          # Static dashboard
│   │       └── application.properties
│   └── test/
│       └── java/com/example/helpdesk/service/
│           └── ChamadoServiceTest.java
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

> A global exception handler is a planned improvement. Today, the service raises a custom exception for missing resources on update/delete, but it is not yet mapped to a specific HTTP status.

---

## Domain and business rules

Tickets use three status values:

- `ABERTO` (open)
- `EM_ANDAMENTO` (in progress)
- `FECHADO` (closed)

Rules implemented in `ChamadoService`:

- New tickets default to `ABERTO` and receive `dataCriacao`.
- Closing a ticket fills `dataFechamento` with the current timestamp.
- Reopening a ticket clears `dataFechamento`.
- Listing returns tickets ordered by creation date, newest first.
- Updating without sending a status keeps the current status.

---

## API endpoints

Base URL: `http://localhost:8080`

| Method | Route | Description |
| :--- | :--- | :--- |
| `GET` | `/chamados` | List tickets, newest first |
| `GET` | `/chamados/{id}` | Get a ticket by ID |
| `GET` | `/chamados/status/{status}` | Filter by status |
| `POST` | `/chamados` | Create a ticket |
| `PUT` | `/chamados/{id}` | Update a ticket |
| `DELETE` | `/chamados/{id}` | Delete a ticket |

### Create a ticket

```http
POST /chamados
Content-Type: application/json
```

```json
{
  "titulo": "Erro ao acessar o sistema",
  "descricao": "O usuário recebe erro 500 ao tentar fazer login."
}
```

Response (`201 Created`):

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

### Swagger UI

With the application running, open:

```text
http://localhost:8080/swagger-ui.html
```

---

## Running with Docker Compose

Requirements: Docker and Docker Compose.

1. Create the environment file and adjust the values if needed:

   ```powershell
   Copy-Item .env.example .env
   ```

2. Start the containers:

   ```bash
   docker compose up --build -d
   ```

3. Open the API at `http://localhost:8080`. MySQL is exposed on port `3306`.

4. Stop the containers:

   ```bash
   docker compose down
   ```

The `.env.example` file contains local development values only. Do not commit a real `.env`.

---

## Running locally

Requirements: Java 21, MySQL 8 and Maven.

1. Create the database:

   ```sql
   CREATE DATABASE helpdesk_pro;
   ```

2. Set the database connection variables:

   ```powershell
   $env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/helpdesk_pro?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo"
   $env:SPRING_DATASOURCE_USERNAME = "root"
   $env:SPRING_DATASOURCE_PASSWORD = "your_password"
   ```

3. Start the application:

   ```bash
   mvn spring-boot:run
   ```

---

## Tests

The current test suite covers the service layer:

- listing tickets;
- creating a ticket with the default status;
- closing a ticket and filling the closing date;
- updating a ticket without losing the current status.

Run:

```bash
mvn test
```

The tests do not cover the HTTP layer or the validation constraints yet.

---

## What I practiced

- Layered REST API organization with controller, service and repository.
- JPA entity mapping, derived queries and transaction boundaries.
- Bean Validation at the API boundary.
- Unit tests with JUnit 5 and Mockito.
- Running a Spring Boot application and MySQL with Docker Compose.

---

## Next steps

- Add a global exception handler to return consistent JSON errors and `404` for missing resources.
- Add controller/integration tests.
- Add pagination and sorting.
- Add authentication and authorization.
- Add a CI workflow for tests and container builds.
- Add screenshots of the dashboard.

---

## License

MIT. See [LICENSE](LICENSE).

Developed by [Emanuel Vitor](https://github.com/evfernandes-dev).
