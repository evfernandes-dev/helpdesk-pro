# HelpDesk Pro

Sistema simples de gerenciamento de chamados de suporte técnico desenvolvido com Java 21, Spring Boot, Maven e MySQL.

## Tecnologias
- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Web
- Spring Validation
- MySQL
- Maven

## Endpoints REST

| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | /chamados | Lista todos os chamados ordenados por data de criação |
| GET | /chamados/{id} | Busca um chamado por ID |
| GET | /chamados/status/{status} | Filtra chamados por status (ABERTO, EM_ANDAMENTO, FECHADO) |
| POST | /chamados | Cria um novo chamado |
| PUT | /chamados/{id} | Atualiza um chamado existente |
| DELETE | /chamados/{id} | Exclui um chamado |

## Configuração
1. Crie o banco helpdesk_pro no MySQL ou deixe o createDatabaseIfNotExist=true criar automaticamente.
2. Ajuste spring.datasource.username e spring.datasource.password no pplication.properties.
3. Rode a aplicação com mvn spring-boot:run ou pela IDE.