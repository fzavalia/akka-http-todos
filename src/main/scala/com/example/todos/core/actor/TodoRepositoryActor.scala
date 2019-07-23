package com.example.todos.core.actor

import akka.actor.{Actor, Props}
import com.example.todos.core.model.InsertableTodo
import com.example.todos.core.repository.TodoRepository

object TodoRepositoryActor {

  trait Message

  case object List extends Message

  case class Store(t: InsertableTodo) extends Message

  case class Update(id: Long, t: InsertableTodo) extends Message

  case class Find(id: Long) extends Message

  case class Delete(id: Long) extends Message

  def props(repository: TodoRepository): Props = Props(new TodoRepositoryActor(repository))
}

class TodoRepositoryActor(repository: TodoRepository) extends Actor {

  import TodoRepositoryActor._

  override def receive: Receive = {
    case List => sender() ! repository.list
    case Store(t) => sender() ! repository.store(t)
    case Update(id, t) => sender() ! repository.update(id, t)
    case Find(id) => sender() ! repository.find(id)
    case Delete(id) => sender() ! repository.delete(id)
  }
}
