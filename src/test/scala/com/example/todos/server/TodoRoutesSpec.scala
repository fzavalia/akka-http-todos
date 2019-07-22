package com.example.todos.server

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.example.todos.core.repository.{TodoNotFound, TodoRepository}
import com.example.todos.core.model.{InsertableTodo, Todo}
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}
import java.time.LocalDate

class TodoRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with JsonSupport with BeforeAndAfter {

  private val routes = new TodoRoutes(MockTodoRepository).make

  "TodoRoutes" should {

    "have an endpoint to store Todos" in {
      Post("/todos", makeInsertableTodo) ~> routes ~> check {
        response.status shouldBe StatusCodes.Accepted
      }
    }

    "have an endpoint to list Todos" in {
      Get("/todos") ~> routes ~> check {
        responseAs[List[Todo]].length shouldBe 1
      }
    }

    "have an endpoint to find Todos" in {

      Get("/todos/1") ~> routes ~> check {
        responseAs[Todo] shouldBe MockTodoRepository.testTodo
      }

      Get("/todos/2") ~> routes ~> check {
        response.status shouldBe StatusCodes.NotFound
      }
    }

    "have an endpoint to delete Todos" in {

      Delete("/todos/1") ~> routes ~> check {
        response.status shouldBe StatusCodes.Accepted
      }

      Delete("/todos/2") ~> routes ~> check {
        response.status shouldBe StatusCodes.NotFound
      }
    }

    "have an endpoint to update Todos" in {

      Put("/todos/1", makeInsertableTodo) ~> routes ~> check {
        response.status shouldBe StatusCodes.Accepted
      }

      Put("/todos/2", makeInsertableTodo) ~> routes ~> check {
        response.status shouldBe StatusCodes.NotFound
      }
    }
  }

  private def makeInsertableTodo = InsertableTodo(completed = false, "description", LocalDate.now)
}

object MockTodoRepository extends TodoRepository {

  val testTodo = Todo(1, completed = true, "description", LocalDate.now)

  override def list: List[Todo] = List(testTodo)

  override def find(id: Long): Option[Todo] = if (id == 1) Some(testTodo) else None

  override def store(todo: InsertableTodo): Unit = ()

  override def update(id: Long, todo: InsertableTodo): Either[Throwable, Unit] = if (id == 1) Right() else Left(new TodoNotFound)

  override def delete(id: Long): Either[Throwable, Unit] = if (id == 1) Right() else Left(new TodoNotFound)
}
