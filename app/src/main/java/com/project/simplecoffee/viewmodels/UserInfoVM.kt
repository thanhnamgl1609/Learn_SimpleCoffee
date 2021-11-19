package com.project.simplecoffee.viewmodels

import androidx.lifecycle.*
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.common.toCustomString
import com.project.simplecoffee.constant.ErrorConstant
import com.project.simplecoffee.domain.models.UserInfo
import com.project.simplecoffee.data.repository.UserRepo
import com.project.simplecoffee.domain.models.details.Gender
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import com.project.simplecoffee.views.main.MainContainer
import com.project.simplecoffee.views.main.MainFragment
import kotlinx.coroutines.*
import javax.inject.Inject

class UserInfoVM @Inject constructor(
    private val container: MainContainer,
    private val userRepo: UserRepo
) : ViewModel() {
    private var userInfoRepo: IUserInfoRepo? = null
    private val userInfoLiveData = MutableLiveData<UserInfo>()

    // Binding data
    var firstName = Transformations.map(userInfoLiveData) { userInfo -> userInfo.firstname }
    var lastName: LiveData<String> =
        Transformations.map(userInfoLiveData) { userInfo -> userInfo.lastname }
    var gender = MediatorLiveData<Int>().apply {
        addSource(userInfoLiveData) {
            if (it.male!!) postValue(Gender.Male.index)
            else postValue(Gender.Female.index)
        }
    }
    var email = MutableLiveData<String>().apply {
        postValue(userRepo.getCurrentUser()?.email)
    }
    var dob: LiveData<String> =
        Transformations.map(userInfoLiveData) { userInfo ->
            userInfo.dob?.toDate().toCustomString()
        }

    init {
        viewModelScope.launch {
            userInfoRepo = userRepo.getUserInfoRepo().data!!
            when (val result = userInfoRepo?.getUserInfo()) {
                is Resource.OnSuccess -> {
                    userInfoLiveData.postValue(result.data!!)

                }
                is Resource.OnFailure -> {
                    container.showMessage(ErrorConstant.ERROR_UNEXPECTED)
                    container.loadFragment(MainFragment.Setting.createFragment())
                }
            }
        }
    }

    fun signOut() {
        userRepo.signOut()
    }
}
