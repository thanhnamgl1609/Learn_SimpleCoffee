package com.project.simplecoffee.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.data.repository.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepo
) : ViewModel() {
    private val _userData: MutableLiveData<FirebaseUser>? = null
    private val userData get() = _userData!!
    private val _loggedStatus = MutableLiveData<Boolean>()
    val loggedStatus : LiveData<Boolean> get() = _loggedStatus

    // For databinding
    val email = MutableLiveData<String>()
    val pwd = MutableLiveData<String>()
    val notifyTxt = MutableLiveData<String>()

    internal val btnSignInVisibility = MutableLiveData(View.VISIBLE)

    init {
        _loggedStatus.postValue(repository.logIn())
    }

    fun getUserData(): LiveData<FirebaseUser> {
        return userData
    }

    // View binding
    fun onSignInClick() {
        val inputMail = email.value ?: ""
        val inputPWD = pwd.value ?: ""
        Log.d("Mail:", inputMail)
        Log.d("Pwd:",inputPWD)
        if (inputMail.isNotEmpty() && inputPWD.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.signIn(inputMail, inputPWD)
                    notifyTxt.postValue("Success to sign in")
                    _loggedStatus.postValue(true)
                } catch (e: Exception) {
                    notifyTxt.postValue("Username or password is not correct")
                    Log.d("FAIL", e.message.toString())
                }
            }
        }
        else
            notifyTxt.postValue("Please enter all fields")
    }
}