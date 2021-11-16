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

    var name: LiveData<String> =
        Transformations.map(userInfo) { userInfo -> "${userInfo?.firstname} ${userInfo?.lastname}" }

    fun signOut() {
        authRepo.signOut()
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
}