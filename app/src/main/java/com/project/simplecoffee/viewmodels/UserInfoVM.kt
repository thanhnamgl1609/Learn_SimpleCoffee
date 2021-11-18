package com.project.simplecoffee.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.domain.models.UserInfo
import com.project.simplecoffee.data.repository.UserInfoRepo
import com.project.simplecoffee.data.repository.UserRepo
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class UserInfoVM @Inject constructor(
    private val userRepo: UserRepo,
    private val userInfoRepo: IUserInfoRepo? = userRepo.getUserInfoRepo().data
) : ViewModel() {
    private var _userInfo = MutableLiveData<UserInfo>()
    private val userInfo: LiveData<UserInfo> get() = _userInfo

    var firstName = MediatorLiveData<String>()

    var lastName = MediatorLiveData<String>()
    var email = MutableLiveData<String>().apply {
        postValue(userRepo.getCurrentUser()?.email)
    }
    var dob = Transformations.map(userInfo) { userInfo -> userInfo.dob?.toDate().toString() }
    var gender = Transformations.map(userInfo) { userInfo -> userInfo.isMale }

    init {
        firstName.addSource(userInfo) { userInfo -> firstName.setValue(userInfo.firstname!!) }
        lastName.addSource(userInfo) { userInfo -> lastName.setValue(userInfo.lastname!!) }
    }

    fun signOut() {
        userRepo.signOut()
        clearUserData()
        checkLogInStatus()
    }

    fun getUserInfo() {

    }

    fun checkLogInStatus() {

    }

    private fun clearUserData() {
        // User info

        // Cart

        // Contact

    }
}