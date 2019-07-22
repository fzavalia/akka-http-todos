package com.example.todos.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, path}
import akka.http.scaladsl.server.{Route, StandardRoute}
import com.example.todos.core.model.{InsertableTodo, Todo}
import com.example.todos.core.repository.{TodoNotFound, TodoRepository}

class TodoRoutes(todoRepository: TodoRepository) extends JsonSupport {

  def make: Route = path("todos") {
    post {
      entity(as[InsertableTodo]) { todo =>
        storeTodoHandler(todo)
      }
    } ~ get {
      listTodosHandler
    }
  } ~ path("todos" / LongNumber) { id =>
    get {
      findTodoHandler(id)
    } ~ put {
      entity(as[InsertableTodo]) { todo =>
        updateTodoHandler(id, todo)
      }
    } ~ delete {
      deleteTodoHandler(id)
    }
  }

  private def listTodosHandler: StandardRoute =
    complete(todoRepository.list)

  private def storeTodoHandler(todo: InsertableTodo): StandardRoute = {
    todoRepository.store(todo)
    complete(HttpResponse(StatusCodes.Accepted, entity = "Accepted"))
  }

  private def findTodoHandler(id: Long): StandardRoute =
    todoRepository.find(id) match {
      case Some(todo) => complete(todo)
      case None => complete(HttpResponse(StatusCodes.NotFound, entity = "Not Found"))
    }

  private def updateTodoHandler(id: Long, todo: InsertableTodo): StandardRoute = {
    todoRepository.update(id, todo) match {
      case Right(_) => complete(HttpResponse(StatusCodes.Accepted))
      case Left(e) => e match {
        case _: TodoNotFound => complete(HttpResponse(StatusCodes.NotFound))
      }
    }
  }


  private def deleteTodoHandler(id: Long): StandardRoute =
    todoRepository.delete(id) match {
      case Right(_) => complete(HttpResponse(StatusCodes.Accepted))
      case Left(e) => e match {
        case _: TodoNotFound => complete(HttpResponse(StatusCodes.NotFound))
      }
    }
}
