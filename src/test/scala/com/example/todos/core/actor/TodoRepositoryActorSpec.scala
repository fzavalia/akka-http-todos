package com.example.todos.core.actor

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.example.todos.test.MockTodoRepository
import org.scalatest.{AsyncWordSpecLike, BeforeAndAfterAll, Matchers}
import akka.pattern.ask
import com.example.todos.core.model.Todo

class TodoRepositoryActorSpec extends TestKit(ActorSystem("MySpec"))
  with DefaultTimeout
  with ImplicitSender
  with AsyncWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  val repository: ActorRef = system.actorOf(TodoRepositoryActor.props(MockTodoRepository))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "TestRepositoryActor" should {

    "find a todo" in {
      val f = repository ? TodoRepositoryActor.Find(1)
      f.mapTo[Option[Todo]].map(_.get shouldBe MockTodoRepository.testTodo)
    }

    "store a todo" in {
      val f = repository ? TodoRepositoryActor.Store(MockTodoRepository.testInsertableTodo)
      f.map(_ shouldBe())
    }

    "list todos" in {
      val f = repository ? TodoRepositoryActor.List
      f.mapTo[List[Todo]].map(_ shouldBe List(MockTodoRepository.testTodo))
    }

    "delete a todo" in {
      val f = repository ? TodoRepositoryActor.Delete(1)
      f.map(_ shouldBe Right())
    }

    "update a todo" in {
      val f = repository ? TodoRepositoryActor.Update(1, MockTodoRepository.testInsertableTodo)
      f.map(_ shouldBe Right())
    }
  }
}
