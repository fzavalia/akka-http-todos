package com.example.todos.server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, path}
import akka.stream.ActorMaterializer
import com.example.todos.core.model.Todo
import com.example.todos.core.repository.{InMemoryTodoRepository, TodoNotFound}
import java.time._

import scala.io.StdIn

object Server extends App with JsonSupport {

  implicit val system = ActorSystem("actorSystem")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val todoRepository = new InMemoryTodoRepository

  def makeTestTodo = Todo(-1, completed = false, "description", LocalDate.now())

  val routes =
    path("todos") {
      post {
        entity(as[Todo]) { todo =>
          todoRepository.store(todo)
          complete(HttpResponse(StatusCodes.Accepted, entity = "Accepted"))
        }
      } ~
        get {
          complete(todoRepository.list)
        }
    } ~
      path("todos" / LongNumber) { id =>
        get {
          todoRepository.find(id) match {
            case Some(todo) => complete(todo)
            case None => complete(HttpResponse(StatusCodes.NotFound, entity = "Not Found"))
          }
        } ~
          put {
            entity(as[Todo]) { todo =>
              todoRepository.update(todo) match {
                case Right(_) => complete(HttpResponse(StatusCodes.Accepted))
                case Left(e) => e match {
                  case _: TodoNotFound => complete(HttpResponse(StatusCodes.NotFound))
                }
              }
            }
          } ~
          delete {
            todoRepository.delete(id) match {
              case Right(_) => complete(HttpResponse(StatusCodes.Accepted))
              case Left(e) => e match {
                case _: TodoNotFound => complete(HttpResponse(StatusCodes.NotFound))
              }
            }
          }
      }

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

  println("Server online at http://localhost:8080\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
