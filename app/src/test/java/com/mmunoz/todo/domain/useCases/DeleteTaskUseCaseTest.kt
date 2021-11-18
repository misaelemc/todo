package com.mmunoz.todo.domain.useCases

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
class DeleteTaskUseCaseTest {

    @Mock
    private lateinit var repository: TaskRepository

    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val key = "key"
    private val oldImages = listOf<String>()

    @Before
    fun setup() {
        deleteTaskUseCase = DeleteTaskUseCase(repository)
    }

    @Test
    fun `when invoke is called then verifies delete in repository is called`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            deleteTaskUseCase.invoke(key, oldImages)
            Mockito.verify(repository).delete(key, oldImages)
        }

    @Test
    fun `given a success result when invoke is called then validate that result and mock are equal`() =
        runBlocking<Unit>(
            Dispatchers.IO
        ) {
            val resultExpected = MutableStateFlow(Response.Success(Unit))
            Mockito.`when`(repository.delete(key, oldImages))
                .thenReturn(resultExpected)

            val result = deleteTaskUseCase.invoke(key, oldImages)
            Assert.assertEquals(resultExpected, result)
        }
}