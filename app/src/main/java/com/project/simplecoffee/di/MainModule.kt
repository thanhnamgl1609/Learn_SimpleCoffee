package com.project.simplecoffee.di

import android.app.Activity
import com.project.simplecoffee.data.mapper.OrderItemMapper
import com.project.simplecoffee.data.mapper.OrderMapper
import com.project.simplecoffee.data.repository.ContactRepo
import com.project.simplecoffee.data.repository.DrinkRepo
import com.project.simplecoffee.data.repository.RevenueRepo
import com.project.simplecoffee.domain.repository.IContactRepo
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.domain.repository.IRevenueRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.presentation.common.main.MainContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object MainModule {
    @Provides
    fun provideMainContainer(activity: Activity): MainContainer =
        activity as MainContainer
}