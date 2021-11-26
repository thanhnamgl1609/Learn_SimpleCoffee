package com.project.simplecoffee.viewmodel

import androidx.lifecycle.*
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.common.toCustomString
import com.project.simplecoffee.constant.ErrorConst
import com.project.simplecoffee.constant.SuccessConst
import com.project.simplecoffee.domain.models.UserInfo
import com.project.simplecoffee.repository.UserRepo
import com.project.simplecoffee.domain.models.details.Gender
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import com.project.simplecoffee.views.main.MainContainer
import com.project.simplecoffee.views.main.AllMainFragment
import kotlinx.coroutines.*
import javax.inject.Inject

class UserInfoVM @Inject constructor(
    private val container: MainContainer,
    private val userRepo: UserRepo
) : ViewModel() {
    private var userInfoRepo: IUserInfoRepo? = null
    private val userInfoLiveData = MutableLiveData<UserInfo>()

    // Binding data
    var firstName = MediatorLiveData<String>().apply {
        addSource(userInfoLiveData) { userInfo -> postValue(userInfo.firstname) }
    }
    var lastName = MediatorLiveData<String>().apply {
        addSource(userInfoLiveData) { userInfo -> postValue(userInfo.lastname) }
    }
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
            userInfo.dob?.toCustomString()
        }

    /**
     * Load user info binding to UI
     */
    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            userInfoRepo = userRepo.getUserInfoRepo().data!!
            when (val result = userInfoRepo?.getUserInfo()) {
                is Resource.OnSuccess -> {
                    userInfoLiveData.value = result.data!!
                }
                is Resource.OnFailure -> {
                    container.showMessage(ErrorConst.ERROR_UNEXPECTED)
                    container.loadFragment(AllMainFragment.Setting.createFragment())
                }
            }
        }
    }

    /**
     * Update user information
     */
    fun onUpdateClick() = viewModelScope.launch {
        userInfoRepo?.apply {
            when (val result = updateUserInfo(
                firstName.value,
                lastName.value,
                gender.value == Gender.Male.index
            )) {
                is Resource.OnSuccess -> {
                    userInfoLiveData.value = result.data!!
                    container.showMessage(SuccessConst.SUCCESS_UPDATE)
                }
                is Resource.OnFailure -> {
                    container.showMessage(result.message.toString())
                }
            }
        } ?: container.showMessage(ErrorConst.ERROR_UNEXPECTED)
    }
}
