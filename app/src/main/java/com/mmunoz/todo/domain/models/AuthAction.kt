package com.mmunoz.todo.domain.models

import androidx.annotation.StringRes

sealed class AuthAction {
    object Success : AuthAction()
    class Error(@StringRes val message: Int) : AuthAction()
    class ErrorMessage(val message: String) : AuthAction()
}
