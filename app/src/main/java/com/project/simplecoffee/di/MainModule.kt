package com.project.simplecoffee.di

import android.app.Activity
import com.project.simplecoffee.views.main.MainContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainModule {
    @Provides
    fun provideMainContainer(activity: Activity) : MainContainer =
        activity as MainContainer
}