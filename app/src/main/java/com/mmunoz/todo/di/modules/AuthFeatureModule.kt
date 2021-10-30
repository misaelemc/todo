package com.mmunoz.todo.di.modules

import androidx.lifecycle.ViewModel
import com.mmunoz.todo.di.scopes.FragmentScope
import com.mmunoz.todo.di.scopes.ViewModelKey
import com.mmunoz.todo.ui.fragments.LoginFragment
import com.mmunoz.todo.ui.fragments.RegisterFragment
import com.mmunoz.todo.ui.viewModels.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AuthFeatureModule {

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun bindLoginFragment(): LoginFragment

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun bindRegisterFragment(): RegisterFragment

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel
}
