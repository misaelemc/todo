package com.mmunoz.todo.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mmunoz.todo.R
import com.mmunoz.todo.data.models.AuthAction
import com.mmunoz.todo.data.repositories.DatabaseRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _liveData = MutableLiveData<AuthAction>()
    val liveData: LiveData<AuthAction> = _liveData

    fun login(user: String, password: String) {
        if (password.isEmpty() || password.isEmpty()) {
            _liveData.value = AuthAction.Error(R.string.todo_empty_fields_error)
        } else {
            databaseRepository.login(user, password) {
                if (it != null) {
                    _liveData.value = AuthAction.ErrorMessage(it)
                } else {
                    _liveData.value = AuthAction.Success
                }
            }
        }
    }

    fun register(user: String, password: String, confirmedPassword: String) {
        if (password.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()) {
            _liveData.value = AuthAction.Error(R.string.todo_empty_fields_error)
        } else if (user.isNotEmpty() && password != confirmedPassword) {
            _liveData.value = AuthAction.Error(R.string.todo_passwords_error)
        } else {
            databaseRepository.register(user, password) {
                if (it != null) {
                    _liveData.value = AuthAction.ErrorMessage(it)
                } else {
                    _liveData.value = AuthAction.Success
                }
            }
        }
    }
}