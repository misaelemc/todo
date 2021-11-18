package com.mmunoz.todo.domain.useCases

import android.location.Location
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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
class AddTaskUseCaseTest {

    @Mock
    private lateinit var repository: TaskRepository

    @Mock
    lateinit var location: Location

    private lateinit var addTaskUseCase: AddTaskUseCase

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        addTaskUseCase = AddTaskUseCase(repository)
    }

    @Test
    fun `when invoke is called then verifies add in repository is called`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            addTaskUseCase.invoke(TASK_NAME, TASK_DESCRIPTION, TASK_IMAGES, location)
            Mockito.verify(repository).add(TASK_NAME, TASK_DESCRIPTION, TASK_IMAGES, location)
        }

    @Test
    fun `given a success result when invoke is called then validate that result and mock are equal`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            val resultExpected = MutableStateFlow(Response.Success(Unit))
            Mockito.`when`(repository.add(TASK_NAME, TASK_DESCRIPTION, TASK_IMAGES, location))
                .thenReturn(resultExpected)

            val result = addTaskUseCase.invoke(TASK_NAME, TASK_DESCRIPTION, TASK_IMAGES, location)
            Assert.assertEquals(resultExpected, result)
        }

    companion object {
        const val TASK_NAME = "name"
        val TASK_IMAGES = listOf<Uri>()
        const val TASK_DESCRIPTION = "description"
    }
}