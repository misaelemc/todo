package com.mmunoz.todo.presentation.tasks

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmunoz.todo.R
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.models.TaskArgs
import com.mmunoz.todo.domain.useCases.AddTaskUseCase
import com.mmunoz.todo.domain.useCases.DeleteTaskUseCase
import com.mmunoz.todo.domain.useCases.EditTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private var args: TaskArgs? = null

    val nameTask: MutableStateFlow<String> = MutableStateFlow("")
    val imageTask: MutableStateFlow<Uri?> = MutableStateFlow(null)
    val descriptionTask: MutableStateFlow<String> = MutableStateFlow("")

    private val _taskState = MutableLiveData<Response<Void>>()
    val taskState: LiveData<Response<Void>> = _taskState

    private val _taskErrorState = MutableLiveData<Int>()
    val taskErrorState: LiveData<Int> = _taskErrorState

    fun setArgs(args: TaskArgs?) {
        this.args = args
        this.args?.let {
            nameTask.value = it.name
            descriptionTask.value = it.description
        }
    }

    fun onSaveClick() {
        if (args == null) {
            add()
        } else {
            edit(args!!.key, args!!.image.orEmpty())
        }
    }

    fun delete(key: String, oldImage: String) = viewModelScope.launch {
        deleteTaskUseCase.invoke(key, oldImage).collect {
            _taskState.value = it
        }
    }

    private fun add() = viewModelScope.launch {
        if (nameTask.value.isEmpty() || descriptionTask.value.isEmpty() || imageTask.value == null) {
            _taskErrorState.value = R.string.todo_empty_fields_error
        } else {
            addTaskUseCase.invoke(nameTask.value, descriptionTask.value, imageTask.value!!)
                .collect {
                    _taskState.value = it
                }
        }
    }

    private fun edit(
        key: String,
        oldImage: String
    ) = viewModelScope.launch {
        if (nameTask.value.isEmpty() || descriptionTask.value.isEmpty() || imageTask.value == null) {
            _taskErrorState.value = R.string.todo_empty_fields_error
        } else {
            editTaskUseCase.invoke(
                key,
                nameTask.value,
                imageTask.value!!,
                descriptionTask.value,
                oldImage
            ).collect {
                _taskState.value = it
            }
        }
    }
}