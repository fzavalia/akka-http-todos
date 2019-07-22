package com.example.todos.core.repository

import com.example.todos.core.model.{InsertableTodo, Todo}

class TodoNotFound extends Throwable

trait TodoRepository {

  def list: List[Todo]

  def find(id: Long): Option[Todo]

  def store(todo: InsertableTodo): Unit

  def update(id: Long, todo: InsertableTodo): Either[Throwable, Unit]

  def delete(id: Long): Either[Throwable, Unit]
}
