package com.mmunoz.todo.presentation.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.auth.AuthResult
import com.mmunoz.todo.R
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.useCases.LoginUseCase
import com.mmunoz.todo.domain.useCases.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @Mock
    lateinit var loginUseCase: LoginUseCase

    @Mock
    lateinit var registerUseCase: RegisterUseCase

    @Mock
    private lateinit var authState: Observer<Response<AuthResult>>

    @Mock
    private lateinit var authErrorState: Observer<Int>

    @Mock
    lateinit var authResult: AuthResult

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel

    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = AuthViewModel(loginUseCase, registerUseCase)
        viewModel.authState.observeForever(authState)
        viewModel.authErrorState.observeForever(authErrorState)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when login is called then verifies that emits errors because of empty fields`() =
        runBlockingTest {
            viewModel.login()
            verify(authErrorState).onChanged(R.string.todo_empty_fields_error)
        }

    @Test
    fun `when login is called then verifies that emits a successfully response`() =
        runBlockingTest {
            viewModel.username.value = "misaelmce@gmail.com"
            viewModel.password.value = "12345"

            val success = Response.Success(authResult)
            val resultExpected = MutableStateFlow(success)
            Mockito.`when`(
                loginUseCase.invoke(viewModel.username.value, viewModel.password.value)
            ).thenReturn(resultExpected)

            viewModel.login()
            verify(authState).onChanged(success)
        }

    @Test
    fun `when register is called then verifies that emits errors because of empty fields`() =
        runBlockingTest {
            viewModel.register()
            verify(authErrorState).onChanged(R.string.todo_empty_fields_error)
        }

    @Test
    fun `when register is called then verifies that emits errors because passwords don't match`() =
        runBlockingTest {
            viewModel.username.value = "misaelmce@gmail.com"
            viewModel.password.value = "12345"
            viewModel.confirmedPassword.value = "123345"
            viewModel.register()
            verify(authErrorState).onChanged(R.string.todo_passwords_error)
        }

    @Test
    fun `when register is called then verifies that emits a successfully response`() =
        runBlockingTest {
            viewModel.username.value = "misaelmce@gmail.com"
            viewModel.password.value = "12345"
            viewModel.confirmedPassword.value = "12345"

            val success = Response.Success(authResult)
            val resultExpected = MutableStateFlow(success)
            Mockito.`when`(
                registerUseCase.invoke(viewModel.username.value, viewModel.password.value)
            ).thenReturn(resultExpected)

            viewModel.register()
            verify(authState).onChanged(success)
        }
}