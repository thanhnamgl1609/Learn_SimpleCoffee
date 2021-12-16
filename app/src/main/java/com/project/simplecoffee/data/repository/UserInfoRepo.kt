package com.project.simplecoffee.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.common.toTimestamp
import com.project.simplecoffee.utils.constant.ErrorConst
import com.project.simplecoffee.domain.model.details.Role
import com.project.simplecoffee.domain.model.UserInfo
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

class UserInfoRepo @Inject constructor(
    private val userRepo: IUserRepo
) : IUserInfoRepo {
    private val collection = Firebase.firestore.collection("userinfo")

    override suspend fun createUserInfo(
        id: String,
        firstName: String,
        lastName: String,
        gender: Boolean,
        dob: LocalDate
    ): Resource<UserInfo?> = withContext(Dispatchers.IO) {
        try {
            val userInfo = UserInfo(
                firstName,
                lastName,
                Role.Customer.value,
                dob.toTimestamp(),
                UserInfo.AVATAR_DEFAULT,
                gender
            ).withId<UserInfo>(id)
            collection.document(id).set(userInfo!!).await()

            Resource.OnSuccess(userInfo)
        } catch (e: Exception) {
            Resource.OnFailure(
                null,
                e.message ?: ErrorConst.ERROR_UNEXPECTED
            )
        }
    }

    override suspend fun updateUserInfo(
        id: String,
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ): Resource<UserInfo?> = withContext(Dispatchers.IO) {
        try {
            val changeInfo = mapOf<String, Any?>(
                "firstname" to firstName,
                "lastname" to lastName,
                "male" to gender
            )
            collection.document(id).update(changeInfo).await()
            Resource.OnSuccess(
                collection.document(id).get().await()
                    .toObject(UserInfo::class.java)
                    ?.withId(id)
            )
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun deleteUserInfo(id: String): Resource<UserInfo?> =
        withContext(Dispatchers.IO) {
            try {
                collection.document(id).delete().await()
                Resource.OnSuccess(null)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun getUserInfo(id: String)
            : Resource<UserInfo?> = withContext(Dispatchers.IO) {
        try {
            val document = collection.document(id).get().await()
            val userInfo = document.toObject(UserInfo::class.java)?.withId<UserInfo>(document.id)
            Resource.OnSuccess(userInfo)
        } catch (e: Exception) {
            Resource.OnFailure(null, e.message.toString())
        }
    }
}