# Help Desk System

Simple REST API for support ticket management built with Java 21, Spring Boot 3.4, Spring Data JPA and MySQL. It includes ticket CRUD, a status lifecycle, validation, Swagger UI and a static dashboard for testing the API in the browser.

> Study/portfolio project. It has no authentication and is not production ready.

## Screenshots

Dashboard:

![Dashboard](docs/img/dashboard.png)

Swagger UI:

![Swagger UI](docs/img/swagger.png)

## Features

- Create, list, fetch by ID or status, update and delete tickets
- New tickets start with status `ABERTO` and receive a creation date
- Closing a ticket fills the closing date; reopening clears it
- Bean Validation for title and description
- `GET /chamados/{id}` returns `404 Not Found` when the ticket does not exist
- OpenAPI documentation with Springdoc (Swagger UI)
- Static dashboard in `src/main/resources/static`
- Docker Compose with MySQL, healthcheck and persistent volume
- Unit tests for the service layer with JUnit 5 and Mockito

## Stack

Java 21, Spring Boot 3.4.5, Spring Data JPA, Hibernate, MySQL 8, Jakarta Bean Validation, Springdoc OpenAPI, Maven, JUnit 5, Mockito, Docker and Docker Compose.

## Running

### Docker

Requirements: Docker and Docker Compose.

```powershell
Copy-Item .env.example .env
docker compose up --build -d
```

- Dashboard: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`
- API: `http://localhost:8080/chamados`

```bash
docker compose down
```

The `.env.example` contains local values only. Never commit `.env`.

### Local

Requirements: Java 21, Maven and MySQL 8.

```sql
CREATE DATABASE helpdesk_pro;
```

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/helpdesk_pro?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo"
$env:SPRING_DATASOURCE_USERNAME = "root"
$env:SPRING_DATASOURCE_PASSWORD = "your_password"
mvn spring-boot:run
```

## Endpoints

Base URL: `http://localhost:8080`

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/chamados` | List tickets, newest first |
| `GET` | `/chamados/{id}` | Fetch a ticket by ID |
| `GET` | `/chamados/status/{status}` | Filter by status |
| `POST` | `/chamados` | Create a ticket |
| `PUT` | `/chamados/{id}` | Update a ticket |
| `DELETE` | `/chamados/{id}` | Delete a ticket |

Statuses: `ABERTO`, `EM_ANDAMENTO`, `FECHADO`.

```http
POST /chamados
Content-Type: application/json

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

## Structure

```text
src/main/java/com/example/helpdesk/
├── controller/   REST endpoints
├── model/        Chamado entity and StatusChamado enum
├── repository/   Spring Data JPA interfaces
└── service/      Business rules
```

The `Chamado` entity is also used as the request body to keep the project simple. A larger project would use separate DTOs.

## Tests

```bash
mvn test
```

Four service-layer tests cover listing, creation, closing and updating without changing the status. The HTTP layer has no tests yet.

## Next steps

- Authentication and authorization
- Controller and integration tests
- Pagination and sorting
- CI to run tests and build the Docker image

## License

MIT. See [LICENSE](LICENSE).

Built by [Emanuel Vitor](https://github.com/evfernandes-dev).
