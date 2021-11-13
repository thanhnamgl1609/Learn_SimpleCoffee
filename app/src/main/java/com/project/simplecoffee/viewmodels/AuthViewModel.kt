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
    private val loggedStatus: Boolean = repository.isLogIn()

    // For databinding
    val email = MutableLiveData<String>()
    val pwd = MutableLiveData<String>()
    val notify_txt = MutableLiveData<String>()

    internal val btnSignInVisibility = MutableLiveData(View.VISIBLE)

    init {
        email.postValue("ABC")
        pwd.postValue("XYZ")
    }

    fun getUserData(): LiveData<FirebaseUser> {
        return userData
    }

    fun getLoggedStatus(): Boolean {
        return loggedStatus
    }

    fun OnSignInClick() {
        val inputMail = email.value ?: ""
        val inputPWD = pwd.value ?: ""
        Log.d("Mail:", inputMail)
        Log.d("Pwd:",inputPWD)
        if (inputMail.isNotEmpty() && inputPWD.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.signIn(inputMail, inputPWD)
                    notify_txt.postValue("Success to sign in")
                } catch (e: Exception) {
                    notify_txt.postValue("Username or password is not correct")
                    Log.d("FAIL", e.message.toString())
                }
            }
        }
        else
            notify_txt.postValue("Please enter all fields")

    }
}