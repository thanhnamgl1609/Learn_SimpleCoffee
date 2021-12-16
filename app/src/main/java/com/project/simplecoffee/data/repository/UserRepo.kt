package com.project.simplecoffee.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.project.simplecoffee.data.mapper.UserMapper
import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import com.project.simplecoffee.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject


class UserRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val mapper: UserMapper
) : IUserRepo {

    override suspend fun signIn(
        email: String,
        pwd: String
    ): Resource<User?> = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, pwd).await()
            val user = mapper.toModel(auth.currentUser)
            Resource.OnSuccess(user)
        } catch (e: Exception) {
            Resource.OnFailure(
                null,
//                ErrorConst.ERROR_LOG_IN
                e.message.toString()
            )
        }
    }

    override suspend fun signUp(
        email: String,
        pwd: String
    ): Resource<User?> = withContext(Dispatchers.IO) {
        try {
            auth.createUserWithEmailAndPassword(email, pwd).await()
            val user = mapper.toModel(auth.currentUser)
            Resource.OnSuccess(user)
        } catch (e: Exception) {
            Resource.OnFailure(
                null,
                e.message ?: ErrorConst.ERROR_UNEXPECTED
            )
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteCurrentUser(): Resource<User?> =
        withContext(Dispatchers.IO) {
            try {
                auth.currentUser!!.delete().await()
                Resource.OnSuccess(null)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun getCurrentUser(): User? {
        return mapper.toModel(auth.currentUser)
    }

    override suspend fun changePassword(newPwd: String): Resource<User?> {
        TODO("Not yet implemented")
    }
}