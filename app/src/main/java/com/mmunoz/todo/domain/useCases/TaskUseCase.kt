package com.mmunoz.todo.domain.useCases

import android.location.Location
import android.net.Uri
import com.mmunoz.todo.domain.repositories.TaskRepository

class AddTaskUseCase constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(
        name: String,
        description: String,
        images: List<Uri>,
        location: Location? = null
    ) = repository.add(name, description, images, location)
}

class DeleteTaskUseCase constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(key: String, currentImages: List<String>) =
        repository.delete(key, currentImages)
}

class EditTaskUseCase constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(
        key: String,
        name: String,
        description: String,
        images: List<Uri>,
        currentImages: List<String>
    ) = repository.edit(key, name, description, images, currentImages)
}