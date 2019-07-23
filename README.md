# TODOS

## About

Project developed for pedagogic purposes in order to understand more about `akka`, `akka-http`, `gradle`, `sbt` (previously) and `scala` in general.

## Commands

### Test

`$ ./gradlew test`

### Build

`$ ./gradlew shadowJar`

## Requirements

Make a routing hierarchy for BREADing `Todos` with `akka-http`

| Action | Method | Path       |
|--------|--------|------------|
| Browse | GET    | /todos     |
| Read   | GET    | /todos/:id |
| Edit   | PUT    | /todos/:id |
| Add    | POST   | /todos     |
| Delete | DELETE | /todos/:id |

Implement at least 2 actors from `akka` which should communicate with each other.

`Todos` should be stored in a MySQL database.

Add a `Dockerfile` to the project and run the server through it.

Passable test coverage.