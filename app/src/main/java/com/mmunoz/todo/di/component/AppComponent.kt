package com.mmunoz.todo.di.component

import com.mmunoz.todo.ToDoApp
import com.mmunoz.todo.di.modules.ActivityBuilder
import com.mmunoz.todo.di.modules.FactoryModule
import com.mmunoz.todo.ui.activities.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        FactoryModule::class,
        ActivityBuilder::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<ToDoApp> {

    fun inject(app: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: ToDoApp): Builder
    }
}