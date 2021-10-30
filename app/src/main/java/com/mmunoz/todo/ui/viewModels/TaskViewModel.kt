package com.mmunoz.todo.ui.viewModels

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mmunoz.todo.R
import com.mmunoz.todo.data.repositories.DatabaseRepository
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _liveData = MutableLiveData<TaskAction>()
    val liveData: LiveData<TaskAction> = _liveData

    fun addTask(name: String, description: String, imageUri: Uri?) {
        if (name.isEmpty() || description.isEmpty() || imageUri == null) {
            _liveData.value = TaskAction.Error(R.string.todo_empty_fields_error)
        } else {
            repository.addTask(name, description, imageUri) {
                defaultResponse(it)
            }
        }
    }

    fun editTask(key: String, name: String, description: String, oldImage: String, imageUri: Uri?) {
        if (name.isEmpty() || description.isEmpty() || imageUri == null) {
            _liveData.value = TaskAction.Error(R.string.todo_empty_fields_error)
        } else {
            repository.editTask(key, name, description, imageUri, oldImage) {
                defaultResponse(it)
            }
        }
    }

    fun deleteTask(key: String, oldImage: String) {
        repository.deleteTask(key, oldImage) {
            defaultResponse(it)
        }
    }

    private fun defaultResponse(it: String?) {
        if (it != null) {
            _liveData.value = TaskAction.ErrorMessage(it)
        } else {
            _liveData.value = TaskAction.Success
        }
    }

    sealed class TaskAction {
        object Success : TaskAction()
        class Error(@StringRes val message: Int) : TaskAction()
        class ErrorMessage(val message: String) : TaskAction()
    }
}