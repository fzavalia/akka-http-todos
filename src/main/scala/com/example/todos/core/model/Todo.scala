package com.example.todos.core.model

import java.time.LocalDate

case class Todo(id: Long, completed: Boolean, description: String, date: LocalDate)
