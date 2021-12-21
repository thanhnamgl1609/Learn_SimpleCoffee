package com.project.simplecoffee.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.mapper.*
import com.project.simplecoffee.data.repository.*
import com.project.simplecoffee.domain.model.OrderItem
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
    fun provideOrderRepo(
        orderMapper: OrderMapper,
        orderItemMapper: OrderItemMapper
    ) = OrderRepo(orderMapper, orderItemMapper) as IOrderRepo

    @Provides
    @Singleton
    fun provideRevenueRepo(orderMapper: OrderMapper) = RevenueRepo(orderMapper) as IRevenueRepo

    @Provides
    fun provideContactRepo(userRepo: IUserRepo) = ContactRepo(userRepo) as IContactRepo

    @Provides
    @Singleton
    fun provideCartMapper(contactRepo: IContactRepo, orderItemMapper: OrderItemMapper) =
        CartMapper(contactRepo, orderItemMapper)

    @Provides
    @Singleton
    fun provideCartRepo(cartMapper: CartMapper) = CartRepo(cartMapper) as ICartRepo

    @Provides
    @Singleton
    fun provideTableMapper(revenueRepo: IRevenueRepo) = TableMapper(revenueRepo)

    @Provides
    @Singleton
    fun provideTableRepo(tableMapper: TableMapper) = TableRepo(tableMapper) as ITableRepo
}