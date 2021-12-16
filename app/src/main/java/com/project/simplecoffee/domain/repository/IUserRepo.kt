package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.utils.common.Resource

interface IUserRepo {
    suspend fun signIn(
        email: String,
        pwd: String
    ): Resource<User?>

    suspend fun signUp(
        email: String,
        pwd: String
    ): Resource<User?>

    fun signOut()

    suspend fun deleteCurrentUser(): Resource<User?>

    suspend fun getCurrentUser(): User?

    suspend fun changePassword(newPwd: String): Resource<User?>
}