package com.project.simplecoffee.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.project.simplecoffee.data.models.SealedRole
import com.project.simplecoffee.data.models.UserInfo
import kotlinx.coroutines.tasks.await
import java.util.*

class UserInfoRepo constructor(
    db: FirebaseFirestore,
    val authRepo: AuthRepo
) {
    private val collection = db.collection("userinfo")
    private var userInfo: UserInfo? = null

    suspend fun getUserInfoFromDB() {
        authRepo.getUID()?.let {
            val document = collection.document(it).get().await()
            userInfo = document.toObject<UserInfo>()?.withId(document.id)
        }
    }

    fun getUserInfo(): UserInfo? {
        return userInfo
    }

    fun signUp(firstName: String, lastName: String, dob: Date) {
        TODO("Not yet implemented")
        val userInfo = UserInfo(
            firstName,
            lastName,
            SealedRole.CUSTOMER.role,
            Timestamp(dob),
            mutableListOf()
        )
        authRepo.getUID()?.let {
            collection.document(it).set(userInfo)
        }
    }
}