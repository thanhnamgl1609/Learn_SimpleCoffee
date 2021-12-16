package com.project.simplecoffee.presentation.user

import androidx.lifecycle.*
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.common.toCustomString
import com.project.simplecoffee.utils.constant.ErrorConst
import com.project.simplecoffee.utils.constant.SuccessConst
import com.project.simplecoffee.domain.model.UserInfo
import com.project.simplecoffee.domain.model.details.Gender
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.user.GetCurrentUserInfoUseCase
import com.project.simplecoffee.domain.usecase.user.UpdateCurrentUserInfoUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import kotlinx.coroutines.*
import javax.inject.Inject

class UserInfoVM @Inject constructor(
    private val container: MainContainer,
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateCurrentUserInfoUseCase: UpdateCurrentUserInfoUseCase
) : ViewModel() {
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
        viewModelScope.launch {
            postValue(getCurrentUserUseCase()?.email)
        }
    }
    var dob: LiveData<String> =
        Transformations.map(userInfoLiveData) { userInfo ->
            userInfo.dob?.toCustomString()
        }
    var url = MediatorLiveData<String>().apply {
        addSource(userInfoLiveData) { userInfo ->
            userInfo.avatar?.let { avatar -> postValue(avatar) }
                ?: run { postValue(UserInfo.AVATAR_DEFAULT) }
        }
    }

    /**
     * Load user info binding to UI
     */
    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            when (val result = getCurrentUserInfoUseCase()) {
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
        when (val result = updateCurrentUserInfoUseCase(
            firstName.value,
            lastName.value,
            gender.value == Gender.Male.index
        )) {
            is Resource.OnSuccess -> {
                container.showMessage(SuccessConst.SUCCESS_UPDATE)
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }
}
