# Help Desk

[![build](https://github.com/evfernandes-dev/helpdesk-pro/actions/workflows/ci.yml/badge.svg)](https://github.com/evfernandes-dev/helpdesk-pro/actions/workflows/ci.yml)

Small support ticket app with a REST API and a built-in web page.
Built with Java 21, Spring Boot 3.4, Spring Data JPA and MySQL.

> Study project. No authentication; not production ready.

## Features

- Create, list, filter, update and delete tickets
- Change only the status with `PATCH /chamados/{id}/status?status=FECHADO`
- Closing a ticket sets the closing date; reopening clears it
- Validation for title and description
- Simple JSON errors, e.g. `{ "erro": "Chamado não encontrado com ID: 99" }`
- Web page at `http://localhost:8080/` to open, filter and manage tickets

## Stack

Java 21, Spring Boot 3.4.5, Spring Data JPA, MySQL 8, Maven, JUnit 5, Mockito, Docker Compose and plain HTML/CSS/JS.

## Running

### Docker

```powershell
Copy-Item .env.example .env
docker compose up --build -d
```

Open `http://localhost:8080/`.

```bash
docker compose down
```

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
| `GET` | `/chamados/status/{status}` | Filter by status, newest first |
| `POST` | `/chamados` | Create a ticket |
| `PUT` | `/chamados/{id}` | Update a ticket |
| `PATCH` | `/chamados/{id}/status?status=FECHADO` | Change only the status |
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

## Tests

```bash
mvn test
```

## License

MIT. See [LICENSE](LICENSE).
