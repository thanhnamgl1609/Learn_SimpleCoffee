package com.project.simplecoffee.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.common.toTimestamp
import com.project.simplecoffee.constant.ErrorConst
import com.project.simplecoffee.domain.models.details.Role
import com.project.simplecoffee.domain.models.UserInfo
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.time.LocalDate

class UserInfoRepo constructor(
    private val uid: String
) : IUserInfoRepo {
    private val collection = Firebase.firestore.collection("userinfo")
    private var userInfo: UserInfo? = null

    override suspend fun createUserInfo(
        firstName: String?,
        lastName: String?,
        dob: LocalDate?,
        gender: Boolean?
    ): Resource<UserInfo> = withContext(Dispatchers.IO) {
        try {
            if (!isValid(firstName, lastName, gender)
                || dob == null
            ) {
                throw IllegalArgumentException()
            }
            userInfo = UserInfo(
                firstName,
                lastName,
                Role.Customer.value,
                dob.toTimestamp(),
                UserInfo.CONSTANT.AVATAR_DEFAULT,
                gender
            ).withId(uid)
            collection.document(uid).set(userInfo!!).await()

            Resource.OnSuccess(userInfo)
        } catch (e: IllegalArgumentException) {
            userInfo = null
            Resource.OnFailure(
                userInfo,
                ErrorConst.NOT_ALL_FILLED
            )
        } catch (e: Exception) {
            userInfo = null
            Resource.OnFailure(
                userInfo,
                e.message ?: ErrorConst.ERROR_UNEXPECTED
            )
        }
    }

    override suspend fun updateUserInfo(
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ): Resource<UserInfo> = withContext(Dispatchers.IO) {
        // Save fields for recovering
        val recoverName = arrayOf(
            userInfo?.firstname,
            userInfo?.lastname
        )
        val recoverGender = userInfo?.male

        try {
            if (!isValid(firstName, lastName, gender)) {
                throw IllegalArgumentException()
            }
            userInfo?.apply {
                firstname = firstName
                lastname = lastName
                male = gender
            }
            collection.document(uid).set(userInfo!!).await()

            Resource.OnSuccess(userInfo)
        } catch (e: IllegalArgumentException) {
            Resource.OnFailure(
                userInfo,
                ErrorConst.NOT_ALL_FILLED
            )
        } catch (e: Exception) {
            // Recovery if error happens
            userInfo?.apply {
                firstname = recoverName[0]
                lastname = recoverName[1]
                male = recoverGender
            }
            Resource.OnFailure(
                userInfo,
                ErrorConst.ERROR_UNEXPECTED
            )
        }
    }

    override suspend fun getUserInfo()
            : Resource<UserInfo?> = withContext(Dispatchers.IO) {
        try {
            if (userInfo == null) {
                val document = collection.document(uid).get().await()
                userInfo = document.toObject(UserInfo::class.java)?.withId(document.id)
            }
            Resource.OnSuccess(userInfo)
        } catch (e: Exception) {
            Resource.OnFailure(null, e.message.toString())
        }
    }

    private fun isValid(
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ): Boolean {
        return firstName != null && lastName != null && gender != null
    }
}