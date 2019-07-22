package com.example.todos.core.repository

import com.example.todos.core.model.Todo

import scala.collection.mutable.ListBuffer

class InMemoryTodoRepository extends TodoRepository {

  private var todos: ListBuffer[Todo] = ListBuffer.empty

  private var count: Long = 1

  override def find(id: Long): Option[Todo] = todos.find(x => x.id == id)

  override def store(todo: Todo): Unit = {
    todos += Todo(count, todo.completed, todo.description, todo.date)
    count += 1
  }

  override def list: List[Todo] = todos.toList

  override def update(todo: Todo): Either[Throwable, Unit] =
    todoIndexWhere(x => x.id == todo.id) match {
      case Some(i) => Right(todos.update(i, todo))
      case None => Left(new TodoNotFound)
    }

  override def delete(id: Long): Either[Throwable, Unit] =
    todoIndexWhere(x => x.id == id) match {
      case Some(i) => Right(todos.remove(i))
      case None => Left(new TodoNotFound)
    }

  private def todoIndexWhere(f: Todo => Boolean): Option[Int] = {
    val i = todos.indexWhere(f)
    if (i != -1) Some(i) else None
  }
}

