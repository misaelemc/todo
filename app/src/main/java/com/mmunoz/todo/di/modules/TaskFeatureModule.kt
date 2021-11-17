package com.mmunoz.todo.di.modules

import androidx.lifecycle.ViewModel
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl
import com.mmunoz.todo.di.scopes.FragmentScope
import com.mmunoz.todo.di.scopes.ViewModelKey
import com.mmunoz.todo.domain.repositories.TaskRepository
import com.mmunoz.todo.domain.useCases.AddTaskUseCase
import com.mmunoz.todo.domain.useCases.DeleteTaskUseCase
import com.mmunoz.todo.domain.useCases.EditTaskUseCase
import com.mmunoz.todo.presentation.tasks.MyTasksFragment
import com.mmunoz.todo.presentation.tasks.TaskCreatorDialog
import com.mmunoz.todo.presentation.tasks.TaskViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TaskFeatureModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [TaskProviderModule::class])
    abstract fun bindMyTasksFragment(): MyTasksFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [TaskProviderModule::class])
    abstract fun bindTaskCreatorDialog(): TaskCreatorDialog

    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}

@Module
object TaskProviderModule {

    @Provides
    fun provideAddTaskUseCase(repository: TaskRepository): AddTaskUseCase {
        return AddTaskUseCase(repository)
    }

    @Provides
    fun provideEditTaskUseCase(repository: TaskRepository): EditTaskUseCase {
        return EditTaskUseCase(repository)
    }

    @Provides
    fun provideDeleteTaskUseCase(repository: TaskRepository): DeleteTaskUseCase {
        return DeleteTaskUseCase(repository)
    }

    @IntoMap
    @Provides
    @ViewModelKey(TaskViewModel::class)
    fun getTaskViewModel(
        addTaskUseCase: AddTaskUseCase,
        editTaskUseCase: EditTaskUseCase,
        deleteTaskUseCase: DeleteTaskUseCase
    ): ViewModel {
        return TaskViewModel(addTaskUseCase, editTaskUseCase, deleteTaskUseCase)
    }
}