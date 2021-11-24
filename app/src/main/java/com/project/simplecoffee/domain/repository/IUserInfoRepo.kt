package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.UserInfo
import java.time.LocalDate
import java.util.*

interface IUserInfoRepo {
    suspend fun createUserInfo(
        firstName: String?,
        lastName: String?,
        dob: LocalDate?,
        gender: Boolean?
    ): Resource<UserInfo>


    suspend fun updateUserInfo(
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ): Resource<UserInfo>

    suspend fun getUserInfo(): Resource<UserInfo?>
}