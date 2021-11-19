package com.project.simplecoffee.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.data.repository.UserInfoRepo
import com.project.simplecoffee.domain.models.Order
import com.project.simplecoffee.domain.models.UserInfo
import com.project.simplecoffee.repository.CartRepo
import com.project.simplecoffee.repository.ContactRepo
import java.util.*

interface IUserRepo {
    suspend fun signIn(
        email: String,
        pwd: String
    ): Resource<FirebaseUser?>

    suspend fun signUp(
        email: String?,
        pwd: String?,
        confirmPWD: String?,
        firstName: String?,
        lastName: String?,
        gender: String?,
        dob: Date?
    ): Resource<FirebaseUser?>

    fun signOut()

    fun getCurrentUser(): FirebaseUser?

    suspend fun changePassword(newPwd: String): Resource<FirebaseUser?>

    fun getUserInfoRepo(): Resource<IUserInfoRepo>

    fun getCartRepo(): Resource<ICartRepo>

    fun getContactRepo(): Resource<IContactRepo>

    fun getOrderRepo(): Resource<IOrderRepo>
}