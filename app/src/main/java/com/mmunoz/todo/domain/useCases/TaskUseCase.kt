package com.mmunoz.todo.domain.useCases

import android.net.Uri
import com.mmunoz.todo.domain.repositories.TaskRepository

class AddTaskUseCase constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(
        name: String,
        description: String,
        image: Uri
    ) = repository.add(name, description, image)
}

class DeleteTaskUseCase constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(
        key: String,
        oldImage: String
    ) = repository.delete(key, oldImage)
}

class EditTaskUseCase constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(
        key: String,
        name: String,
        image: Uri,
        description: String,
        oldImage: String
    ) = repository.edit(key, name, description, image, oldImage)
}