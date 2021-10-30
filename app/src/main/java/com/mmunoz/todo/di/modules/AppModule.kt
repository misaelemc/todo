package com.mmunoz.todo.di.modules

import android.app.Application
import android.content.Context
import com.mmunoz.todo.ToDoApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun providesContext(app: ToDoApp): Context = app.applicationContext

    @Provides
    @Singleton
    fun providesApplication(app: ToDoApp): Application = app
}