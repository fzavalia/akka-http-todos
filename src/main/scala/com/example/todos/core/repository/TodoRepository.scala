package com.example.todos.core.repository

import com.example.todos.core.model.Todo

class TodoNotFound extends Throwable

trait TodoRepository {

  def list: List[Todo]

  def find(id: Long): Option[Todo]

  def store(todo: Todo): Unit

  def update(todo: Todo): Either[Throwable, Unit]

  def delete(id: Long): Either[Throwable, Unit]
}
