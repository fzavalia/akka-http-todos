package com.example.todos.test

import java.time.LocalDate

import com.example.todos.core.model.{InsertableTodo, Todo}
import com.example.todos.core.repository.{TodoNotFound, TodoRepository}

object MockTodoRepository extends TodoRepository {

  val testTodo = Todo(1, completed = true, "description", LocalDate.now)

  val testInsertableTodo = InsertableTodo(testTodo.completed, testTodo.description, testTodo.date)

  override def list: List[Todo] = List(testTodo)

  override def find(id: Long): Option[Todo] = if (id == 1) Some(testTodo) else None

  override def store(todo: InsertableTodo): Unit = ()

  override def update(id: Long, todo: InsertableTodo): Either[Throwable, Unit] = if (id == 1) Right(()) else Left(new TodoNotFound)

  override def delete(id: Long): Either[Throwable, Unit] = if (id == 1) Right(()) else Left(new TodoNotFound)
}
