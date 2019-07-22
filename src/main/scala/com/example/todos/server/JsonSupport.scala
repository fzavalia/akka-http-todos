package com.example.todos.server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.example.todos.core.model.Todo
import java.time._
import java.time.format.DateTimeFormatter

import spray.json.DefaultJsonProtocol
import spray.json._

import scala.util.Try

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val localDateFormat = new JsonFormat[LocalDate] {

    override def write(obj: LocalDate): JsValue = JsString(formatter.format(obj))

    override def read(json: JsValue): LocalDate = {
      json match {
        case JsString(lDString) =>
          Try(LocalDate.parse(lDString, formatter)).getOrElse(deserializationError(deserializationErrorMessage))
        case _ => deserializationError(deserializationErrorMessage)
      }
    }

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    private val deserializationErrorMessage =
      s"Expected date time in ISO offset date time format ex. ${LocalDate.now().format(formatter)}"
  }

  implicit val todoFormat: RootJsonFormat[Todo] = jsonFormat4(Todo)
}