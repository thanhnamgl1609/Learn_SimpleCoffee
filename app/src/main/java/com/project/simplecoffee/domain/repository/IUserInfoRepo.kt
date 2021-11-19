package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.UserInfo
import java.util.*

interface IUserInfoRepo {
    suspend fun createUserInfo(
        firstName: String?,
        lastName: String?,
        dob: Date?,
        gender: String?
    ): Resource<UserInfo>


    suspend fun updateUserInfo(
        firstName: String?,
        lastName: String?,
        gender: String?
    ): Resource<UserInfo>

    suspend fun getUserInfo(): Resource<UserInfo?>
}