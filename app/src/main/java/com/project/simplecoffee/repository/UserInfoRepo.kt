package com.project.simplecoffee.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.constant.ErrorConstant
import com.project.simplecoffee.domain.models.details.Role
import com.project.simplecoffee.domain.models.UserInfo
import com.project.simplecoffee.domain.models.details.Gender
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.util.*

class UserInfoRepo constructor(
    private val uid: String
) : IUserInfoRepo {
    private val collection = Firebase.firestore.collection("userinfo")
    private var userInfo: UserInfo? = null

    override suspend fun createUserInfo(
        firstName: String?,
        lastName: String?,
        dob: Date?,
        gender: String?
    ): Resource<UserInfo> = withContext(Dispatchers.IO) {
        try {
            if (
                firstName == null || lastName == null
                || dob == null || gender == null
            ) {
                throw IllegalArgumentException()
            }
            userInfo = UserInfo(
                firstName,
                lastName,
                Role.Customer.value,
                Timestamp(dob),
                UserInfo.CONSTANT.AVATAR_DEFAULT,
                gender == Gender.Male.value,
                mutableListOf()
            ).withId(uid)
            collection.document(uid).set(userInfo!!).await()

            Resource.OnSuccess(userInfo)
        } catch (e: IllegalArgumentException) {
            Resource.OnFailure(
                userInfo,
                ErrorConstant.NOT_ALL_FILLED
            )
        } catch (e: Exception) {
            Resource.OnFailure(
                userInfo,
                e.message ?: ErrorConstant.ERROR_UNEXPECTED
            )
        }
    }

    override suspend fun updateUserInfo(
        firstName: String?,
        lastName: String?,
        gender: String?
    ): Resource<UserInfo> {
        TODO("Not yet implemented")
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
}