package com.mmunoz.todo.data.repositories

import com.google.firebase.auth.AuthResult
import com.mmunoz.todo.data.wrappers.FirebaseAuthWrapper
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val wrapper: FirebaseAuthWrapper
) : AuthRepository {

    override suspend fun login(user: String, password: String): Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)
            val auth = wrapper.signInWithEmailAndPassword(user, password).await()
            emit(Response.Success(auth))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }

    override suspend fun register(
        username: String,
        password: String
    ): Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)
            val auth = wrapper.createUserWithEmailAndPassword(username, password).await()
            emit(Response.Success(auth))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }
}