package com.example.todos.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.example.todos.core.repository.{InMemoryTodoRepository}

import scala.io.StdIn

object Server extends App {

  implicit val system = ActorSystem("actorSystem")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val todoRepository = new InMemoryTodoRepository

  val todoRoutes = new TodoRoutes(todoRepository).make

  val bindingFuture = Http().bindAndHandle(todoRoutes, "localhost", 8080)

  println("Server online at http://localhost:8080\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
