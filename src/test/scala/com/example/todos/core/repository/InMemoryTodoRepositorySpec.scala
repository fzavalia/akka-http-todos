package com.example.todos.core.repository

import java.time.LocalDate

import com.example.todos.core.model.{InsertableTodo, Todo}
import org.scalatest.{Matchers, WordSpec}

class InMemoryTodoRepositorySpec extends WordSpec with Matchers {

  "An InMemoryTodoRepository" can {

    "store a todo" in {
      makeAnInMemoryTodoRepositoryWithAStoredTodo
    }

    "find a stored todo" in {
      val (repo, storedTodo) = makeAnInMemoryTodoRepositoryWithAStoredTodo
      repo.find(1).get shouldBe Todo(1, storedTodo.completed, storedTodo.description, storedTodo.date)
    }

    "delete a stored todo" in {
      val (repo, _) = makeAnInMemoryTodoRepositoryWithAStoredTodo
      repo.delete(1).isRight shouldBe true
    }

    "list stored todos" in {
      val (repo, _) = makeAnInMemoryTodoRepositoryWithAStoredTodo
      repo.list.length shouldBe 1
    }

    "update a stored todo" in {
      val (repo, _) = makeAnInMemoryTodoRepositoryWithAStoredTodo
      val updatedDate = LocalDate.now
      val updatedTodo = InsertableTodo(completed = false, "updated description", updatedDate)
      val updateResult = repo.update(1, updatedTodo)
      updateResult.isRight shouldBe true
      repo.find(1).get shouldBe Todo(1, updatedTodo.completed, updatedTodo.description, updatedTodo.date)
    }
  }

  private def makeAnInMemoryTodoRepositoryWithAStoredTodo: (InMemoryTodoRepository, InsertableTodo) = {
    val repo = new InMemoryTodoRepository
    val todo = InsertableTodo(completed = true, "description", testDate)
    repo.store(todo)
    (repo, todo)
  }

  private val testDate = LocalDate.now
}
