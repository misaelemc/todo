package com.mmunoz.todo.di.modules

import androidx.lifecycle.ViewModel
import com.mmunoz.todo.data.repositories.AuthRepositoryImpl
import com.mmunoz.todo.di.scopes.FragmentScope
import com.mmunoz.todo.di.scopes.ViewModelKey
import com.mmunoz.todo.domain.repositories.AuthRepository
import com.mmunoz.todo.domain.useCases.LoginUseCase
import com.mmunoz.todo.domain.useCases.RegisterUseCase
import com.mmunoz.todo.presentation.auth.AuthViewModel
import com.mmunoz.todo.presentation.auth.LoginFragment
import com.mmunoz.todo.presentation.auth.RegisterFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AuthFeatureModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [AuthProviderModule::class])
    abstract fun bindLoginFragment(): LoginFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [AuthProviderModule::class])
    abstract fun bindRegisterFragment(): RegisterFragment

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}


@Module
object AuthProviderModule {

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @IntoMap
    @Provides
    @ViewModelKey(AuthViewModel::class)
    fun getAuthViewModel(
        loginUseCase: LoginUseCase,
        registerUseCase: RegisterUseCase
    ): ViewModel {
        return AuthViewModel(loginUseCase, registerUseCase)
    }
}