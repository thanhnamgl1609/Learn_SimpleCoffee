package com.project.simplecoffee.presentation.common.setting

import android.view.View
import androidx.lifecycle.*
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.details.Role
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.auth.SignOutUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingVM @Inject constructor(
    private val container: MainContainer,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val _isSignedIn = MutableLiveData<Boolean>().apply {
        viewModelScope.launch { postValue(getCurrentUserUseCase() != null) }
    }
    private val _staffVisible = MutableLiveData(View.GONE)
    private val _managerVisible = MutableLiveData(View.GONE)
    private val _customerVisible = MutableLiveData(View.GONE)
    private val _role = MutableLiveData<String>()

    val staffVisible: LiveData<Int>
        get() = _staffVisible
    val managerVisible: LiveData<Int>
        get() = _managerVisible
    val customerVisible: LiveData<Int>
        get() = _customerVisible
    val role: LiveData<String>
        get() = _role

    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn

    fun onOrderHistoryClick() {
        if (!_isSignedIn.value!!) {
            container.onSignIn()
        } else {
            moveTo(AllMainFragment.OrderHistory)
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
        signOutUseCase()
        checkSignedInStatus()
    }

    fun onTableStatusClick() {
        moveTo(AllMainFragment.TableStatus)
    }

    fun onStatisticClick() {
        moveTo(AllMainFragment.Revenue)
    }

    fun checkSignedInStatus() = viewModelScope.launch {
        container.toggleNavigationBottom(false)
        val currentUser = getCurrentUserUseCase()
        if (currentUser != null) {
            _isSignedIn.postValue(true)
            currentUser.apply {
                _role.postValue(role?.value.toString())
                when (role) {
                    is Role.Customer -> {
                        _customerVisible.postValue(View.VISIBLE)
                    }
                    is Role.Staff -> {
                        _staffVisible.postValue(View.VISIBLE)
                    }
                    is Role.Manager -> {
                        _managerVisible.postValue(View.VISIBLE)
                    }
                }
            }
        } else {
            _isSignedIn.postValue(false)
            _staffVisible.postValue(View.GONE)
            _managerVisible.postValue(View.GONE)
            _customerVisible.postValue(View.GONE)
        }
    }

    private fun moveTo(frag: AllMainFragment) {
        container.loadFragment(frag.createFragment(), true)
    }
}