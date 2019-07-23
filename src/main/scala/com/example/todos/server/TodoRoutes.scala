package com.example.todos.server

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, path}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.example.todos.core.actor.TodoRepositoryActor
import com.example.todos.core.model.{InsertableTodo, Todo}
import com.example.todos.core.repository.TodoNotFound

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class TodoRoutes(todoRepository: ActorRef)(implicit val ec: ExecutionContext, implicit val timeout: Timeout) extends JsonSupport {

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

  private def listTodosHandler: Route =
    handleRepositoryResponse(askRepository(TodoRepositoryActor.List).mapTo[List[Todo]])(complete(_))

  private def storeTodoHandler(todo: InsertableTodo): Route =
    handleRepositoryResponse(askRepository(TodoRepositoryActor.Store(todo))) { _ =>
      complete(HttpResponse(StatusCodes.Accepted))
    }

  private def findTodoHandler(id: Long): Route =
    handleRepositoryResponse(askRepository(TodoRepositoryActor.Find(id)).mapTo[Option[Todo]]) {
      case Some(todo) => complete(todo)
      case None => complete(HttpResponse(StatusCodes.NotFound))
    }

  private def updateTodoHandler(id: Long, todo: InsertableTodo): Route =
    handleRepositoryResponse(askRepository(TodoRepositoryActor.Update(id, todo)).mapTo[Either[Throwable, Unit]]) {
      case Right(_) => complete(HttpResponse(StatusCodes.Accepted))
      case Left(e) => e match {
        case _: TodoNotFound => complete(HttpResponse(StatusCodes.NotFound))
      }
    }

  private def deleteTodoHandler(id: Long): Route =
    handleRepositoryResponse(askRepository(TodoRepositoryActor.Delete(id)).mapTo[Either[Throwable, Unit]]) {
      case Right(_) => complete(HttpResponse(StatusCodes.Accepted))
      case Left(e) => e match {
        case _: TodoNotFound => complete(HttpResponse(StatusCodes.NotFound))
      }
    }

  private def askRepository(m: TodoRepositoryActor.Message) = todoRepository ? m

  private def handleRepositoryResponse[A](f: Future[A])(success: A => Route) = {
    onComplete(f) {
      case Success(v) => success(v)
      case _ => complete(HttpResponse(StatusCodes.InternalServerError))
    }
  }
}
