package com.mmunoz.todo.presentation.tasks

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mmunoz.todo.R
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.models.TaskArgs
import com.mmunoz.todo.domain.useCases.AddTaskUseCase
import com.mmunoz.todo.domain.useCases.DeleteTaskUseCase
import com.mmunoz.todo.domain.useCases.EditTaskUseCase
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TaskViewModelTest {

    @Mock
    lateinit var addTaskUseCase: AddTaskUseCase

    @Mock
    lateinit var editTaskUseCase: EditTaskUseCase

    @Mock
    lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @Mock
    private lateinit var taskStateObserver: Observer<Response<Unit>>

    @Mock
    private lateinit var taskErrorStateObserver: Observer<Int>

    @Mock
    private lateinit var taskImagesObserver: Observer<List<String>>

    @Mock
    private lateinit var taskImageDeletedObserver: Observer<Uri>

    @Mock
    private lateinit var imageUri: Uri

    private val key = "key"
    private val currentImages = listOf<String>("url")

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TaskViewModel

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = TaskViewModel(addTaskUseCase, editTaskUseCase, deleteTaskUseCase)
        viewModel.taskState.observeForever(taskStateObserver)
        viewModel.taskImages.observeForever(taskImagesObserver)
        viewModel.taskErrorState.observeForever(taskErrorStateObserver)
        viewModel.taskImageDeleted.observeForever(taskImageDeletedObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when onSaveClick is called then verifies that add method emits errors because of empty fields`() =
        runBlockingTest {
            viewModel.onSaveClick()
            Mockito.verify(taskErrorStateObserver).onChanged(R.string.todo_empty_fields_error)
        }

    @Test
    fun `when onSaveClick is called then verifies that add method emits a successfully response`() =
        runBlockingTest {
            viewModel.nameTask.value = "Prueba"
            viewModel.imageTask.value.add(imageUri)
            viewModel.descriptionTask.value = "Description"

            val success = Response.Success(Unit)
            val resultExpected = MutableStateFlow(success)
            Mockito.`when`(
                addTaskUseCase.invoke(
                    viewModel.nameTask.value,
                    viewModel.descriptionTask.value,
                    viewModel.imageTask.value
                )
            ).thenReturn(resultExpected)

            viewModel.onSaveClick()
            Mockito.verify(taskStateObserver).onChanged(success)
        }

    @Test
    fun `when onSaveClick is called then verifies that edit method emits errors because of empty fields`() =
        runBlockingTest {
            mockArgs()
            viewModel.nameTask.value = ""
            viewModel.onSaveClick()
            Mockito.verify(taskErrorStateObserver).onChanged(R.string.todo_empty_fields_error)
        }


    @Test
    fun `when onSaveClick is called then verifies that edit method emits a successfully response`() =
        runBlockingTest {
            mockArgs()
            viewModel.imageTask.value.add(imageUri)

            val success = Response.Success(Unit)
            val resultExpected = MutableStateFlow(success)
            Mockito.`when`(
                editTaskUseCase.invoke(
                    key,
                    viewModel.nameTask.value,
                    viewModel.descriptionTask.value,
                    viewModel.imageTask.value,
                    currentImages
                )
            ).thenReturn(resultExpected)

            viewModel.onSaveClick()
            Mockito.verify(taskStateObserver).onChanged(success)
        }

    @Test
    fun `when delete is called then verifies that emits a successfully response`() =
        runBlockingTest {

            val success = Response.Success(Unit)
            val resultExpected = MutableStateFlow(success)
            Mockito.`when`(deleteTaskUseCase.invoke(key, currentImages))
                .thenReturn(resultExpected)

            viewModel.delete(key, currentImages)
            Mockito.verify(taskStateObserver).onChanged(success)
        }


    private fun mockArgs() {
        val name = "name"
        val description = "description"
        viewModel.setArgs(TaskArgs(key, name, description, currentImages))
    }
}