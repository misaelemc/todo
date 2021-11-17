package com.mmunoz.todo.domain.models

import java.io.Serializable

data class TaskArgs(
    val key: String,
    val name: String,
    val description: String,
    val image: String?
) : Serializable