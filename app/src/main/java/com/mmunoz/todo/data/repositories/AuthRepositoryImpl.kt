package com.mmunoz.todo.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override suspend fun login(user: String, password: String): Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)
            val auth = auth.signInWithEmailAndPassword(user, password)
                .await()
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
            val auth = auth.createUserWithEmailAndPassword(username, password)
                .await()
            emit(Response.Success(auth))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }
}