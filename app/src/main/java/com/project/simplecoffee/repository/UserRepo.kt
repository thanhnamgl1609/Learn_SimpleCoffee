package com.project.simplecoffee.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.constant.ErrorConst
import com.project.simplecoffee.domain.repository.*
import com.project.simplecoffee.repository.CartRepo
import com.project.simplecoffee.repository.ContactRepo
import com.project.simplecoffee.repository.OrderRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*
import javax.inject.Inject


class UserRepo @Inject constructor(
    private val auth: FirebaseAuth,
) : IUserRepo {
    private var user: FirebaseUser? = null
    private var userInfoRepo: IUserInfoRepo? = null
    private var cartRepo: ICartRepo? = null
    private var contactRepo: IContactRepo? = null
    private var orderRepo: IOrderRepo? = null

    override suspend fun signIn(
        email: String,
        pwd: String
    ): Resource<FirebaseUser?> = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, pwd).await()
            user = auth.currentUser
            Resource.OnSuccess(user)
        } catch (e: Exception) {
            Resource.OnFailure(
                null,
                ErrorConst.ERROR_LOG_IN
            )
        }
    }

    override suspend fun signUp(
        email: String?,
        pwd: String?,
        confirmPWD: String?,
        firstName: String?,
        lastName: String?,
        gender: Boolean?,
        dob: Date?
    ): Resource<FirebaseUser?> = withContext(Dispatchers.IO) {
        try {
            if (pwd != confirmPWD)
                throw IllegalArgumentException()

            auth.createUserWithEmailAndPassword(email.toString(), pwd.toString()).await()
            user = auth.currentUser

            getUserInfoRepo()
            when (val result = userInfoRepo!!.createUserInfo(
                firstName, lastName, dob, gender
            )) {
                is Resource.OnSuccess -> Resource.OnSuccess(user)
                is Resource.OnFailure -> {
                    user?.delete()?.await()
                    user = null
                    Resource.OnFailure(null, result.message)
                }

            }
        } catch (e: IllegalArgumentException) {
            Resource.OnFailure(null, ErrorConst.ERROR_DIFF_PWD)
        } catch (e: Exception) {
            Resource.OnFailure(
                null,
                e.message ?: ErrorConst.ERROR_UNEXPECTED
            )
        }
    }

    override fun signOut() {
        auth.signOut()
        user = null
    }

    override fun getCurrentUser(): FirebaseUser? {
        if (auth.currentUser != null) {
            user = auth.currentUser
        }
        return user
    }

    override suspend fun changePassword(newPwd: String): Resource<FirebaseUser?> {
        TODO("Not yet implemented")
    }


    override fun getUserInfoRepo(): Resource<IUserInfoRepo> {
        return try {
            if (userInfoRepo == null) {
                userInfoRepo = UserInfoRepo(user!!.uid)
            }
            Resource.OnSuccess(userInfoRepo)
        } catch (e: Exception) {
            Resource.OnFailure(
                userInfoRepo,
                ErrorConst.ERROR_AUTH
            )
        }
    }

    override fun getCartRepo(): Resource<ICartRepo> {
        return try {
            if (cartRepo == null) {
                cartRepo = CartRepo(user!!.uid)
            }
            Resource.OnSuccess(cartRepo)
        } catch (e: Exception) {
            Resource.OnFailure(
                cartRepo,
                ErrorConst.ERROR_AUTH
            )
        }
    }

    override fun getContactRepo(): Resource<IContactRepo> {
        return try {
            if (contactRepo == null) {
                contactRepo = ContactRepo(user!!.uid, userInfoRepo)
            }
            Resource.OnSuccess(contactRepo)
        } catch (e: Exception) {
            Resource.OnFailure(
                contactRepo,
                ErrorConst.ERROR_AUTH
            )
        }
    }

    override fun getOrderRepo(): Resource<IOrderRepo> {
        return try {
            if (orderRepo == null) {
                orderRepo = OrderRepo(user!!.uid)
            }
            Resource.OnSuccess(orderRepo)
        } catch (e: Exception) {
            Resource.OnFailure(
                orderRepo,
                ErrorConst.ERROR_AUTH
            )
        }
    }
}