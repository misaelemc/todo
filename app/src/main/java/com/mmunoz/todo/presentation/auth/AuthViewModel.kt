package com.mmunoz.todo.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.mmunoz.todo.R
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.useCases.LoginUseCase
import com.mmunoz.todo.domain.useCases.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val username: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
    val confirmedPassword: MutableStateFlow<String> = MutableStateFlow("")

    private val _authState = MutableLiveData<Response<AuthResult>>()
    val authState: LiveData<Response<AuthResult>> = _authState

    private val _authErrorState = MutableLiveData<Int>()
    val authErrorState: LiveData<Int> = _authErrorState

    fun login() = viewModelScope.launch {
        if (username.value.isEmpty() || password.value.isEmpty()) {
            _authErrorState.value = R.string.todo_empty_fields_error
        } else {
            loginUseCase.invoke(username.value, password.value).collect {
                _authState.value = it
            }
        }
    }

    fun register() = viewModelScope.launch {
        if (username.value.isEmpty() || password.value.isEmpty() || confirmedPassword.value.isEmpty()) {
            _authErrorState.value = R.string.todo_empty_fields_error
        } else if (username.value.isNotEmpty() && password.value != confirmedPassword.value) {
            _authErrorState.value = R.string.todo_passwords_error
        } else {
            registerUseCase.invoke(username.value, password.value).collect {
                _authState.value = it
            }
        }
    }
}