package com.project.simplecoffee.di

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.repository.UserRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.auth.AuthContainer
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
        db: FirebaseFirestore,
        auth: FirebaseAuth
    ): IUserRepo = UserRepo(db, auth)
}