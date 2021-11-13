package com.project.simplecoffee.viewmodels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.data.repository.AuthRepo

class ShopViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private var _repository: AuthRepo? = null
    private val repository get() = _repository!!
    private val userData: MutableLiveData<FirebaseUser?>? = null
    private val loggedStatus: MutableLiveData<Boolean?>? = null

    fun getUserData(): MutableLiveData<FirebaseUser?>? {
        return userData
    }

    fun getLoggedStatus(): MutableLiveData<Boolean?>? {
        return loggedStatus
    }

    fun signIn(email: String?, pass: String?) {
        repository?.login(email, pass)
    }
}