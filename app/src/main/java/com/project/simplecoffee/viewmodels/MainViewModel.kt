package com.project.simplecoffee.viewmodels

import android.service.autofill.Transformation
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.data.models.UserInfo
import com.project.simplecoffee.data.repository.AuthRepo
import com.project.simplecoffee.data.repository.UserInfoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val userInfoRepo: UserInfoRepo
) : ViewModel() {
    private var _user = MutableLiveData<FirebaseUser>().apply {
        postValue(authRepo.getUser())
    }
    val user: LiveData<FirebaseUser> get() = _user

    private var _userInfo = MutableLiveData<UserInfo>().apply {
        postValue(userInfoRepo.getUserInfo())
    }
    private val userInfo: LiveData<UserInfo> get() = _userInfo

    var firstName: LiveData<String> =
        Transformations.map(userInfo) { userInfo -> userInfo.firstname }
    var lastName: LiveData<String> =
        Transformations.map(userInfo) { userInfo -> userInfo.lastname }
    var email: LiveData<String> =
        Transformations.map(user) { user -> user.email }
    var dob: LiveData<Date> =
        Transformations.map(userInfo) { userInfo -> userInfo.dob?.toDate() }
    var gender: LiveData<Boolean> =
        Transformations.map(userInfo) { userInfo -> userInfo.gender }

    fun signOut() {
        authRepo.signOut()
        clearUserData()
        checkLogInStatus()
    }

    fun getUserInfo() {
        if (userInfoRepo.getUserInfo() == null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    userInfoRepo.getUserInfoFromDB()
                    Log.d("USER INFO", userInfoRepo.getUserInfo().toString())
                    _userInfo.postValue(userInfoRepo.getUserInfo())
                } catch (e: Exception) {
                    Log.d("User Info", e.message.toString())
                }
            }
        }
    }

    fun checkLogInStatus() {
        _user.postValue(authRepo.getUser())
    }

    private fun clearUserData() {
        // User info
        userInfoRepo.clear()
        _userInfo.postValue(null)
        // Cart

        // Contact

    }
}