package com.project.simplecoffee.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.simplecoffee.data.mapper.UserMapper
import com.project.simplecoffee.data.model.UserInfoDB
import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.domain.model.details.Role
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import com.project.simplecoffee.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDate
import javax.inject.Inject


class UserRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val mapper: UserMapper
) : IUserRepo {
    private val collection = Firebase.firestore.collection("userinfo")
    private var currentUser: User? = null

    override suspend fun signIn(
        email: String,
        pwd: String
    ): Resource<User?> = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, pwd).await()
            currentUser = getUserInfo(auth.currentUser!!.uid).data
            Resource.OnSuccess(currentUser)
        } catch (e: Exception) {
            Resource.OnFailure(
                null,
                e.message.toString()
            )
        }
    }

    override suspend fun signUp(
        email: String,
        pwd: String,
        firstName: String?,
        lastName: String?,
        gender: Boolean?,
        dob: LocalDate?
    ): Resource<User?> = withContext(Dispatchers.IO) {
        try {
            auth.createUserWithEmailAndPassword(email, pwd).await()
            val id = auth.currentUser!!.uid
            val currentUser = User(
                email,
                null,
                firstName,
                lastName,
                Role.Customer,
                dob,
                User.AVATAR_DEFAULT,
                mapper.getGender(gender)
            ).withId<User>(id)
            mapper.fromModel(currentUser)?.apply {
                collection.document(id).set(this).await()
            }
            Resource.OnSuccess(currentUser)
        } catch (e: Exception) {
            Resource.OnFailure(
                null,
                e.message ?: ErrorConst.ERROR_UNEXPECTED
            )
        }
    }

    override fun signOut() {
        currentUser = null
        auth.signOut()
    }

    override suspend fun deleteCurrentUser(): Resource<User?> =
        withContext(Dispatchers.IO) {
            try {
                auth.currentUser!!.delete().await()
                currentUser = null
                Resource.OnSuccess(null)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun getCurrentUser(): User? {
        if (currentUser == null) {
            currentUser = getUserByEmail(auth.currentUser?.email).data
        }
        return currentUser
    }

    override suspend fun getUserByEmail(email: String?): Resource<User?> =
        withContext(Dispatchers.IO) {
            email?.run {
                try {
                    val document = collection.whereEqualTo("email", email).get().await()
                    Resource.OnSuccess(mapper.toModel(
                        document.first()?.run {
                            toObject(UserInfoDB::class.java).withId(id)
                        }
                    ))
                } catch (e: Exception) {
                    Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
                }
            } ?: Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)
        }

    override suspend fun changePassword(newPwd: String): Resource<User?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserInfo(
        id: String,
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ): Resource<User?> = withContext(Dispatchers.IO) {
        try {
            val changeInfo = mapOf<String, Any?>(
                "firstname" to firstName,
                "lastname" to lastName,
                "male" to gender
            )
            collection.document(id).update(changeInfo).await()
            currentUser?.apply {
                firstname = firstName
                lastname = lastName
                male = mapper.getGender(gender)
            }
            Resource.OnSuccess(
                currentUser
            )
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun getUserInfo(id: String)
            : Resource<User?> = withContext(Dispatchers.IO) {
        try {
            val document = collection.document(id).get().await()
            val userInfo =
                document.toObject(UserInfoDB::class.java)?.withId<UserInfoDB>(document.id)
            Resource.OnSuccess(mapper.toModel(userInfo))
        } catch (e: Exception) {
            Log.d("Erro", e.message.toString())
            Resource.OnFailure(null, e.message.toString())
        }
    }
}