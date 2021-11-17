package com.mmunoz.todo.domain.models

class TaskItem(
    val name: String,
    val description: String,
    val images: List<String>,
    val latitude: Double? = null,
    val longitude: Double? = null,
)