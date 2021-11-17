package com.mmunoz.todo.domain.repositories

import com.google.firebase.auth.AuthResult
import com.mmunoz.todo.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(user: String, password: String): Flow<Response<AuthResult>>

    suspend fun register(username: String, password: String): Flow<Response<AuthResult>>

}