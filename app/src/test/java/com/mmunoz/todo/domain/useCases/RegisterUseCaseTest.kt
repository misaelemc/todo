package com.mmunoz.todo.domain.useCases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.AuthResult
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.repositories.AuthRepository
import junit.framework.Assert
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterUseCaseTest {

    @Mock
    private lateinit var repository: AuthRepository

    @Mock
    lateinit var authResult: AuthResult

    private lateinit var registerUseCase: RegisterUseCase

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        registerUseCase = RegisterUseCase(repository)
    }

    @Test
    fun `when invoke is called then verifies register in repository is called`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            registerUseCase.invoke(LoginUseCaseTest.USERNAME, LoginUseCaseTest.PASSWORD)
            Mockito.verify(repository).register(LoginUseCaseTest.USERNAME, LoginUseCaseTest.PASSWORD)
        }

    @Test
    fun `given a success result when invoke is called then validate that result and mock are equal`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            val resultExpected = MutableStateFlow(Response.Success(authResult))
            Mockito.`when`(repository.register(LoginUseCaseTest.USERNAME, LoginUseCaseTest.PASSWORD))
                .thenReturn(resultExpected)

            val result =  registerUseCase.invoke(LoginUseCaseTest.USERNAME, LoginUseCaseTest.PASSWORD)
            assertEquals(resultExpected, result)
        }
}