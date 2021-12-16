package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.UserInfo
import java.time.LocalDate

interface IUserInfoRepo {
    suspend fun createUserInfo(
        id: String,
        firstName: String,
        lastName: String,
        gender: Boolean,
        dob: LocalDate
    ): Resource<UserInfo?>


    suspend fun updateUserInfo(
        id: String,
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ): Resource<UserInfo?>

    suspend fun getUserInfo(id: String): Resource<UserInfo?>

    suspend fun deleteUserInfo(id: String): Resource<UserInfo?>
}