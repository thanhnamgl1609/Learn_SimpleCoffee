package com.project.simplecoffee.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.main.AllMainFragment
import com.project.simplecoffee.views.main.MainContainer
import javax.inject.Inject

class SettingVM @Inject constructor(
    val container: MainContainer,
    val userRepo: IUserRepo
) {
    private val _isSignedIn = MutableLiveData(userRepo.getCurrentUser() != null)

    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn

    fun onOrderHistoryClick() {
        if (!_isSignedIn.value!!) {
            container.onSignIn()
        } else {
            Log.d("INFO", "Signed in")
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

    fun checkSignedInStatus() {
        _isSignedIn.value = (userRepo.getCurrentUser() != null)
    }

    private fun moveTo(frag: AllMainFragment) {
        container.loadFragment(frag.createFragment(), true)
    }
}