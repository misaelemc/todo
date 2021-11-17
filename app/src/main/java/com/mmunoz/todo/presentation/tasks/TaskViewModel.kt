package com.mmunoz.todo.presentation.tasks

import android.location.Location
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

class TaskViewModel constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private var args: TaskArgs? = null
    private var lastKnownLocation: Location? = null

    val nameTask: MutableStateFlow<String> = MutableStateFlow("")
    val imageTask: MutableStateFlow<MutableList<Uri>> = MutableStateFlow(arrayListOf())
    val descriptionTask: MutableStateFlow<String> = MutableStateFlow("")

    private val _taskState = MutableLiveData<Response<Void>>()
    val taskState: LiveData<Response<Void>> = _taskState

    private val _taskErrorState = MutableLiveData<Int>()
    val taskErrorState: LiveData<Int> = _taskErrorState

    private val _taskImages = MutableLiveData<List<String>>()
    val taskImages: LiveData<List<String>> = _taskImages

    private val _taskImageDeleted = MutableLiveData<Uri>()
    val taskImageDeleted: LiveData<Uri> = _taskImageDeleted

    override fun onCleared() {
        super.onCleared()
        args = null
        lastKnownLocation = null
    }

    fun setArgs(args: TaskArgs?) {
        this.args = args
        this.args?.let {
            nameTask.value = it.name
            descriptionTask.value = it.description
            if (!it.images.isNullOrEmpty()) {
                _taskImages.value = it.images!!
            }
        }
    }

    fun setLastKnownLocation(location: Location) {
        lastKnownLocation = location
    }

    fun onSaveClick() {
        if (args == null) {
            add()
        } else {
            edit(args!!.key)
        }
    }

    fun delete(key: String, images: List<String>) = viewModelScope.launch {
        deleteTaskUseCase.invoke(key, images).collect {
            _taskState.value = it
        }
    }

    private fun add() = viewModelScope.launch {
        if (nameTask.value.isEmpty() || descriptionTask.value.isEmpty() || imageTask.value.isEmpty()) {
            _taskErrorState.value = R.string.todo_empty_fields_error
        } else {
            addTaskUseCase.invoke(
                nameTask.value,
                descriptionTask.value,
                imageTask.value,
                lastKnownLocation
            ).collect {
                _taskState.value = it
            }
        }
    }

    private fun edit(key: String) = viewModelScope.launch {
        if (nameTask.value.isEmpty() || descriptionTask.value.isEmpty() || imageTask.value.isEmpty()) {
            _taskErrorState.value = R.string.todo_empty_fields_error
        } else {
            editTaskUseCase.invoke(
                key,
                nameTask.value,
                descriptionTask.value,
                imageTask.value,
                args!!.images.orEmpty()
            ).collect {
                _taskState.value = it
            }
        }
    }
}