package com.project.simplecoffee.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.details.Role
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.main.AllMainFragment
import com.project.simplecoffee.views.main.MainContainer
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingVM @Inject constructor(
    val container: MainContainer,
    val userRepo: IUserRepo
) : ViewModel() {
    private val _isSignedIn = MutableLiveData(userRepo.getCurrentUser() != null)
    private val _staffVisible = MutableLiveData(View.INVISIBLE)
    private val _managerVisible = MutableLiveData(View.INVISIBLE)
    private val _role = MutableLiveData<String>()

    val staffVisible: LiveData<Int>
        get() = _staffVisible
    val managerVisible: LiveData<Int>
        get() = _managerVisible
    val role: LiveData<String>
        get() = _role

    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn

    fun onOrderHistoryClick() {
        if (!_isSignedIn.value!!) {
            container.onSignIn()
        } else {
            TODO()
        }
    }

    fun onAccountInformationClick() {
        if (!_isSignedIn.value!!) {
            container.onSignIn()
        } else
            moveTo(AllMainFragment.AccountInfo)
    }

    fun onSavedAddressClick() {
        if (!_isSignedIn.value!!) {
            container.onSignIn()
        } else {
            moveTo(AllMainFragment.Contact)
        }
    }

    fun onSignInClick() {
        container.onSignIn()
    }

    fun onSignOutClick() {
        userRepo.signOut()
        checkSignedInStatus()
    }

    fun onTableStatusClick() {

    }

    fun onStatisticClick() {
        container.toggleNavigationBottom()
        container.loadFragment(AllMainFragment.Revenue.createFragment(), true)
    }

    fun checkSignedInStatus() = viewModelScope.launch {
        container.toggleNavigationBottom(false)
        _isSignedIn.value = (userRepo.getCurrentUser() != null)
        if (userRepo.getCurrentUser() != null) {
            val userInfoRepo = userRepo.getUserInfoRepo().data!!
            when (val result = userInfoRepo.getUserInfo()) {
                is Resource.OnSuccess -> {
                    val userInfo = result.data!!
                    userInfo.apply {
                        _role.postValue(role.toString())
                        when (role) {
                            Role.Customer.value -> {
                                _staffVisible.postValue(View.GONE)
                            }
                            Role.Staff.value -> {
                                _staffVisible.postValue(View.VISIBLE)
                                _managerVisible.postValue(View.GONE)
                            }
                            Role.Manager.value -> {
                                _staffVisible.postValue(View.VISIBLE)
                                _managerVisible.postValue(View.VISIBLE)
                            }
                        }
                    }
                }
                is Resource.OnFailure -> {
                    _staffVisible.postValue(View.GONE)
                    container.showMessage(result.message.toString())
                }
            }
        } else {
            _staffVisible.postValue(View.GONE)
        }
    }

    private fun moveTo(frag: AllMainFragment) {
        container.loadFragment(frag.createFragment(), true)
    }
}