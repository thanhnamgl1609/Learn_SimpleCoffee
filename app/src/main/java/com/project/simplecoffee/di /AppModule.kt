package com.project.simplecoffee.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.simplecoffee.data.repository.AuthRepo
import com.project.simplecoffee.data.repository.UserInfoRepo
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
    fun provideFirebaseFirestore() = Firebase.firestore

    @Singleton
    @Provides
    fun provideAuthRepo() = AuthRepo()

    @Singleton
    @Provides
    fun provideUserInfoRepo(
        db: FirebaseFirestore,
        authRepo: AuthRepo
    ) =
        UserInfoRepo(db, authRepo)
}