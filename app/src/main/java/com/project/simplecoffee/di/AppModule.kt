package com.project.simplecoffee.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.mapper.DrinkMapper
import com.project.simplecoffee.data.mapper.OrderItemMapper
import com.project.simplecoffee.data.mapper.OrderMapper
import com.project.simplecoffee.data.mapper.UserMapper
import com.project.simplecoffee.data.repository.*
import com.project.simplecoffee.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideUserMapper() = UserMapper()

    @Singleton
    @Provides
    fun provideUserRepo(
        auth: FirebaseAuth,
        userMapper: UserMapper
    ): IUserRepo = UserRepo(auth, userMapper)

    @Singleton
    @Provides
    fun provideUserInfoRepo(
        userRepo: IUserRepo
    ): IUserInfoRepo = UserInfoRepo(userRepo)

    @Singleton
    @Provides
    fun provideDrinkCategoryRepo(): IDrinkCategoryRepo = DrinkCategoryRepo()

    @Singleton
    @Provides
    fun provideDrinkMapper(drinkCategoryRepo: IDrinkCategoryRepo) =
        DrinkMapper(drinkCategoryRepo)

    @Singleton
    @Provides
    fun provideDrinkRepo(drinkMapper: DrinkMapper): IDrinkRepo = DrinkRepo(drinkMapper)

    @Provides
    @Singleton
    fun provideOrderItemMapper(drinkRepo: IDrinkRepo) = OrderItemMapper(drinkRepo)

    @Provides
    @Singleton
    fun provideOrderMapper(orderItemMapper: OrderItemMapper) = OrderMapper(orderItemMapper)

    @Provides
    @Singleton
    fun provideOrderRepo(orderMapper: OrderMapper) =
        OrderRepo(orderMapper) as IOrderRepo

    @Provides
    @Singleton
    fun provideRevenueRepo(orderMapper: OrderMapper) = RevenueRepo(orderMapper) as IRevenueRepo

    @Provides
    fun provideContactRepo(userRepo: IUserRepo) = ContactRepo(userRepo) as IContactRepo
}