# HelpDesk Pro

A simple help desk ticket management system built with Java 21, Spring Boot 3.4.5, Maven and MySQL. 

It has a clean 3-layer structure (Controller - Service - Repository), business rules, validation, unit tests and a simple web interface served statically.

## Features

- Create tickets with title and description
- List all tickets (newest first)
- Filter tickets by status (`ABERTO`, `EM_ANDAMENTO`, `FECHADO`)
- Update ticket info and status
- Automatically sets `dataFechamento` when closing a ticket and clears it when reopening
- Delete tickets
- Built-in web UI at `http://localhost:8080`

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Web
- Spring Data JPA
- Spring Validation
- Hibernate
- MySQL
- JUnit 5 & Mockito
- HTML, CSS & JavaScript (vanilla)

## API Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/chamados` | List all tickets (ordered by creation date desc) |
| GET | `/chamados/{id}` | Get a ticket by ID |
| GET | `/chamados/status/{status}` | Filter by status (`ABERTO`, `EM_ANDAMENTO`, `FECHADO`) |
| POST | `/chamados` | Create a new ticket |
| PUT | `/chamados/{id}` | Update a ticket |
| DELETE | `/chamados/{id}` | Delete a ticket |

## Example JSON

### Create (POST `/chamados`)
```json
{
  "titulo": "Internet not working",
  "descricao": "Can't connect to Wi-Fi since morning"
}
```

### Update (PUT `/chamados/{id}`)
```json
{
  "titulo": "Internet still down",
  "descricao": "Issue persists after router restart",
  "status": "EM_ANDAMENTO"
}
```

## How to run

1. **Clone the repo**
```bash
git clone https://github.com/your-username/helpdesk-pro.git
cd helpdesk-pro
```

2. **Set up MySQL**
   - Create a database called `helpdesk_pro` or just let it be created automatically (it uses `createDatabaseIfNotExist=true`)
   - Update `src/main/resources/application.properties` with your MySQL username and password

3. **Run the app**
```bash
mvn spring-boot:run
```

Or open the project in your IDE and run `HelpdeskApplication.java`

4. **Access it**
   - API: `http://localhost:8080/chamados`
   - Web UI: `http://localhost:8080/`