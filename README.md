# Organizer App – Spring Boot Backend

O aplicație de organizare tip „all-in-one” care integrează funcționalități de **event, note, reminder, task, task list și notificări prin email**.

Proiect realizat pentru cursul **Programare Web**, folosind **Spring Boot** și **PostgreSQL**.

## Funcționalități
- Autentificare și autorizare cu **JWT** (inclusiv claims și roluri)
- **CRUD complet** pentru entități (event, task, note etc.)
- Relații între entități (one-to-one, one-to-many, many-to-many) configurate cu **FluentAPI**
- **DTO-uri** pentru request/response
- **Error handling** standardizat (coduri de eroare)
- **Notificări și emailuri** pentru remindere și taskuri
- Documentație API prin **Swagger/OpenAPI**

## Tehnologii
- Java + Spring Boot
- JPA/Hibernate (Repository Pattern)
- PostgreSQL + Docker Compose
- Liquibase/Flyway (migrări)
- Maven

## Setup

To start working with the backend install docker and docker compose from https://docs.docker.com/engine/install/ and enter the command below to launch the Postgresql database while in the Deployment folder:

```shell showLineNumbers
docker-compose -f .\docker-compose.yml -p mobylab-app-db up -d
```

You can use PGAdmin (https://www.pgadmin.org/) or DBeaver (https://dbeaver.io/download/) to access the database on localhost:5432 with database/user/password "postgres".

In order to run the application you need to have maven installed (https://maven.apache.org/install.html) and run the following commands:

```shell showLineNumbers
mvn clean install
mvn spring-boot:run
```
