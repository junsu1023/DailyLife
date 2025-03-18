package com.example.data.mapper

import com.example.data.entitiy.TodoEntity
import com.example.domain.model.TodoModel

fun TodoModel.convertTodoEntity(): TodoEntity =
    TodoEntity(
        this.id,
        this.dueDate,
        this.isComplete,
        this.icon,
        this.title
    )

fun TodoEntity.convertTodoModel(): TodoModel =
    TodoModel(
        this.id,
        this.dueDate,
        this.isComplete,
        this.icon,
        this.title
    )