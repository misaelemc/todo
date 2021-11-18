package com.mmunoz.todo.domain.useCases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.repositories.TaskRepository
import com.mmunoz.todo.domain.useCases.AddTaskUseCaseTest.Companion.TASK_DESCRIPTION
import com.mmunoz.todo.domain.useCases.AddTaskUseCaseTest.Companion.TASK_IMAGES
import com.mmunoz.todo.domain.useCases.AddTaskUseCaseTest.Companion.TASK_NAME
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
class EditTaskUseCaseTest {

    @Mock
    private lateinit var repository: TaskRepository

    private lateinit var editTaskUseCase: EditTaskUseCase

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val key = "key"
    private val currentImages = listOf<String>()

    @Before
    fun setup() {
        editTaskUseCase = EditTaskUseCase(repository)
    }

    @Test
    fun `when invoke is called then verifies edit in repository is called`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            editTaskUseCase.invoke(key, TASK_NAME, TASK_DESCRIPTION, TASK_IMAGES, currentImages)
            Mockito.verify(repository)
                .edit(key, TASK_NAME, TASK_DESCRIPTION, TASK_IMAGES, currentImages)
        }

    @Test
    fun `given a success result when invoke is called then validate that result and mock are equal`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            val resultExpected = MutableStateFlow(Response.Success(Unit))
            Mockito.`when`(
                repository.edit(
                    key,
                    TASK_NAME,
                    TASK_DESCRIPTION,
                    TASK_IMAGES,
                    currentImages
                )
            ).thenReturn(resultExpected)

            val result =
                editTaskUseCase.invoke(key, TASK_NAME, TASK_DESCRIPTION, TASK_IMAGES, currentImages)
            Assert.assertEquals(resultExpected, result)
        }
}