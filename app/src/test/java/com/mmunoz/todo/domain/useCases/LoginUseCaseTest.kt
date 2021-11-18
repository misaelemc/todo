package com.mmunoz.todo.domain.useCases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.AuthResult
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginUseCaseTest {

    @Mock
    private lateinit var repository: AuthRepository

    @Mock
    lateinit var authResult: AuthResult

    private lateinit var loginUseCase: LoginUseCase

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun `when invoke is called then verifies login in repository is called`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            loginUseCase.invoke(USERNAME, PASSWORD)
            verify(repository).login(USERNAME, PASSWORD)
        }

    @Test
    fun `given a success result when invoke is called then validate that result and mock are equal`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            val resultExpected = MutableStateFlow(Response.Success(authResult))
            Mockito.`when`(repository.login(USERNAME, PASSWORD))
                .thenReturn(resultExpected)

            val result = loginUseCase.invoke(USERNAME, PASSWORD)
            assertEquals(resultExpected, result)
        }

    companion object {

        const val USERNAME = "username"
        const val PASSWORD = "password"

    }
}