package com.mmunoz.todo.di.modules

import androidx.lifecycle.ViewModelProvider
import com.mmunoz.todo.presentation.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class FactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}