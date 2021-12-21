package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.utils.common.Resource
import java.time.LocalDate

interface IUserRepo {
    suspend fun signIn(
        email: String,
        pwd: String
    ): Resource<User?>

    suspend fun signUp(
        email: String,
        pwd: String,
        firstName: String,
        lastName: String,
        gender: Boolean,
        dob: LocalDate
    ): Resource<User?>

    fun signOut()

    suspend fun deleteCurrentUser(): Resource<User?>

    suspend fun getCurrentUser(): User?

    suspend fun getUserByEmail(email: String?): Resource<User?>

    suspend fun changePassword(newPwd: String): Resource<User?>

    suspend fun updateUserInfo(
        id: String,
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ): Resource<User?>

    suspend fun getUserInfo(id: String): Resource<User?>
}