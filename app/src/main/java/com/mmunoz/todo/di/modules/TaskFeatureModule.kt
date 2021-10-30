package com.mmunoz.todo.di.modules

import androidx.lifecycle.ViewModel
import com.mmunoz.todo.di.scopes.FragmentScope
import com.mmunoz.todo.di.scopes.ViewModelKey
import com.mmunoz.todo.ui.fragments.MyTasksFragment
import com.mmunoz.todo.ui.fragments.TaskCreatorDialog
import com.mmunoz.todo.ui.viewModels.TaskViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TaskFeatureModule {

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun bindMyTasksFragment(): MyTasksFragment

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun bindTaskCreatorDialog(): TaskCreatorDialog

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    abstract fun bindMyTasksViewModel(viewModel: TaskViewModel): ViewModel
}