package com.project.simplecoffee.di

import android.app.Activity
import com.project.simplecoffee.views.auth.AuthContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {
    @Provides
    fun provideAuthContainer(activity: Activity) : AuthContainer =
        activity as AuthContainer
}