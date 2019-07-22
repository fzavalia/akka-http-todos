package com.example.todos.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.example.todos.core.repository.InMemoryTodoRepository

import scala.io.StdIn

object Server extends App {

  implicit private val system = ActorSystem("actorSystem")
  implicit private val materializer = ActorMaterializer()
  implicit private val executionContext = system.dispatcher

  private val todoRepository = new InMemoryTodoRepository
  private val todoRoutes = new TodoRoutes(todoRepository).make
  private val bindingFuture = Http().bindAndHandle(todoRoutes, "localhost", 8080)

  println("Server online at http://localhost:8080\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
