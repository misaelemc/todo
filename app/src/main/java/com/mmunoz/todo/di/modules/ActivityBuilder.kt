package com.mmunoz.todo.di.modules

import com.mmunoz.todo.di.scopes.ActivityScope
import com.mmunoz.todo.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [AuthFeatureModule::class, TaskFeatureModule::class])
    abstract fun bindMainActivity(): MainActivity
}