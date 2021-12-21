package com.project.simplecoffee.di

import android.app.Activity
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.domain.usecase.auth.SignInUseCase
import com.project.simplecoffee.domain.usecase.auth.SignOutUseCase
import com.project.simplecoffee.domain.usecase.auth.SignUpUseCase
import com.project.simplecoffee.domain.usecase.user.DeleteCurrentUserUseCase
import com.project.simplecoffee.presentation.auth.AuthContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {
    @Provides
    fun provideAuthContainer(activity: Activity): AuthContainer =
        activity as AuthContainer

    @Provides
    fun provideSignInUseCase(userRepo: IUserRepo) = SignInUseCase(userRepo)

    @Provides
    fun provideDeleteUserUseCase(userRepo: IUserRepo) = DeleteCurrentUserUseCase(userRepo)

    @Provides
    fun provideSignUpUseCase(
        userRepo: IUserRepo,
    ) = SignUpUseCase(userRepo)

    @Provides
    fun provideSignOutUseCase(userRepo: IUserRepo) = SignOutUseCase(userRepo)
}