package com.mmunoz.todo

import android.app.Application
import com.mmunoz.todo.di.component.AppComponent
import com.mmunoz.todo.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class ToDoApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private var component: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initializeInjector()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    private fun initializeInjector() {
        component = DaggerAppComponent.builder()
            .application(this)
            .build()
            .apply { inject(this@ToDoApp) }
    }
}