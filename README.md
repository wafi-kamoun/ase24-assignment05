# TaskBoard (ASE WS 2024/25)

## Project Description

The core idea is to develop a simplified version of task board, similar to GitHub Projects boards, Trello, or Jira.
Users create tasks, move tasks between columns, assign tasks to team members, and delete tasks.

The main domain events are:

* `TaskCreated`
* `TaskAssigned`
* `TaskUpdated` (e.g., changed title, description, status, etc.)
* `TaskDeleted`

Potential extensions:
* `TaskCommented` (e.g., add a comment to a task)

Potential projections:

* A board view of tasks by status/column (e.g., `TODO`, `DOING`, `DONE`).
* A board view per user (a user's personal task list by assignment).
* An audit log per task (who created it, who assigned it, how many times it moved, who deleted it).

## Architecture

The backend is implemented as a Spring Boot application with a PostgreSQL database.
DTOs are used to map the domain model to the REST API.
Bean validation is used to validate the DTOs before processing the requests.
The domain objects are only validated statically using the `@NotNull` annotation (from `org.springframework.lang`).
The application is containerized using Docker and requires a Docker daemon for local testing.

Later, the application will be re-engineering to use CQRS and event sourcing.
Due to the abstraction that the business/domain core provides, this can be done without changing the core.
The controllers/the REST API also won't be affected.
After that re-engineering, the containerized application will be deployed to a cloud environment.
As part of a future assignment, we will also add a frontend.

## Spring Boot Web Application

### Build and start application with dev profile

**Note:** In the `dev` profile, the repositories are cleared before startup and the initial data is loaded (see [`LoadInitialData.java`](https://github.com/se-ubt/ase24-taskboard/blob/main/application/src/main/java/de/unibayreuth/se/taskboard/LoadInitialData.java)).

Build application:
```shell
mvn clean install
```

Start Postgres docker container:
```shell
docker run -d -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16-alpine
```

Start application (data source configured via [`application.yaml`](application/src/main/resources/application.yaml)):
```shell
cd application
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Docker

#### Building an app image from the Dockerfile

```shell
docker build -t taskboard-app:latest .
```

#### Create and run a Docker container based on the image (start DB container first)

```shell
docker network create container-net
docker run -d --name db --net container-net -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16-alpine
docker run --net container-net -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=postgres -it --rm taskboard-app:latest
```

`-it`  runs a container in interactive mode with a pseudo-TTY (terminal).
`--rm` automatically removes the container (and its associated resources) if it exists already.<br/>


#### Using Docker compose to run the app together with the DB container

Build container image:

```shell
docker compose build
```

Create and start containers:

```shell
docker compose up
```

Stop and remove containers and networks:

```shell
docker compose down
```

### REST requests (tasks)

#### Get tasks

All tasks:
```shell
curl http://localhost:8080/api/tasks
```
Task by ID:
```shell
curl http://localhost:8080/api/tasks/4221a32e-3e2a-4bc8-9ae7-8249ea68dfd9 # add valid task id here
```

Tasks by status:
```shell
curl http://localhost:8080/api/tasks/status/TODO # add valid task status here
```

Tasks by assignee:
```shell
curl http://localhost:8080/api/tasks/assignee/4749f527-e240-4b9c-bc6c-5e1b744d553e # add valid user id here
```

#### Create task

```shell
curl --header "Content-Type: application/json" --request POST --data '{"title":"Task title","description":"Task description"}' http://localhost:8080/api/tasks
```

#### Update task

Update title and description:
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":"238ce5b2-9d85-43f7-a90e-172ae3ab0d28","title":"New title","description":"New description"}' http://localhost:8080/api/tasks/238ce5b2-9d85-43f7-a90e-172ae3ab0d28 # add valid task id
```

Update status:
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":"238ce5b2-9d85-43f7-a90e-172ae3ab0d28","title":"New title","description":"New description", "status":"DOING"}' http://localhost:8080/api/tasks/238ce5b2-9d85-43f7-a90e-172ae3ab0d28 # add valid task id
```

Assign user:
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":"238ce5b2-9d85-43f7-a90e-172ae3ab0d28","title":"New title","description":"New description","status":"DOING", "assignee": {"id":"4749f527-e240-4b9c-bc6c-5e1b744d553e", "name":"Charlie"}}' http://localhost:8080/api/tasks/238ce5b2-9d85-43f7-a90e-172ae3ab0d28 # add valid task id and user id
```

### REST requests (users)

#### Get users

All users:

```shell
curl http://localhost:8080/api/users
```

User by ID:

```shell
curl http://localhost:8080/api/users/6900289d-6905-4898-b05a-3d96ededdd73 # add valid user id
```

### Create user

```shell
 curl --header "Content-Type: application/json" --request POST --data '{"name": "Denise"}' http://localhost:8080/api/users  
```
