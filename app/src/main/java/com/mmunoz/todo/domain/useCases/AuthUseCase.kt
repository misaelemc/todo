package com.mmunoz.todo.domain.useCases

import com.mmunoz.todo.domain.repositories.AuthRepository

class LoginUseCase constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(
        user: String,
        password: String
    ) = repository.login(user, password)
}

class RegisterUseCase constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(
        user: String,
        password: String
    ) = repository.register(user, password)
}