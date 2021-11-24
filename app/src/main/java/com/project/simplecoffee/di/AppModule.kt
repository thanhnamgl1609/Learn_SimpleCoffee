package com.project.simplecoffee.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.repository.UserRepo
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.repository.DrinkCategoryRepo
import com.project.simplecoffee.repository.DrinkRepo
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
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideUserRepo(
        auth: FirebaseAuth
    ): IUserRepo = UserRepo(auth)

    @Singleton
    @Provides
    fun provideDrinkRepo(
        db: FirebaseFirestore
    ): IDrinkRepo = DrinkRepo(db)

    @Singleton
    @Provides
    fun provideDrinkCategoryRepo(
        db: FirebaseFirestore
    ): IDrinkCategoryRepo = DrinkCategoryRepo(db)
}