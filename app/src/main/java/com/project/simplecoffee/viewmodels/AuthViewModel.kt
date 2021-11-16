package com.project.simplecoffee.viewmodels

import android.view.View
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.data.repository.AuthRepo
import com.project.simplecoffee.data.repository.UserInfoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val userInfoRepo: UserInfoRepo
) : ViewModel() {
    private var _user = MutableLiveData<FirebaseUser>().apply {
        postValue(authRepo.getUser())
    }
    val user: LiveData<FirebaseUser> get() = _user

    // For databinding
    val email = MutableLiveData<String>()
    val pwd = MutableLiveData<String>()
    val confirmPwd = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val dob = MutableLiveData<Date>()
    val notifyTxt = MutableLiveData<String>()

    internal val btnSignInVisibility = MutableLiveData(View.VISIBLE)

    fun onSignUpClick() {
        val inputMail = email.value ?: ""
        val inputPwd = pwd.value ?: ""
        val inputConfirmPwd = confirmPwd.value ?: ""
        val inputFirstName = firstName.value ?: ""
        val inputLastName = lastName.value ?: ""
        val inputDOB = dob.value

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (inputFirstName.isEmpty() ||
                    inputLastName.isEmpty() ||
                    inputConfirmPwd.isEmpty() ||
                    inputDOB == null
                ) {
                    throw IllegalArgumentException()
                }

                if (inputPwd == inputConfirmPwd) {
                    authRepo.signUp(inputMail, inputPwd)
                    userInfoRepo.signUp(
                        inputFirstName,
                        inputLastName,
                        inputDOB
                    )

                    notifyTxt.postValue("Success to sign up")
                } else
                    notifyTxt.postValue("Password and confirm password are not match")

            } catch (e: IllegalArgumentException) {
                notifyTxt.postValue("Please enter all fields")
            } catch (e: Exception) {
                notifyTxt.postValue("Unexpected error")
            }
        }
    }

    // View binding
    fun onSignInClick() {
        val inputMail = email.value ?: ""
        val inputPWD = pwd.value ?: ""
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepo.signIn(inputMail, inputPWD)
                notifyTxt.postValue("Success to sign in")
                _user.postValue(authRepo.getUser())
            } catch (e: IllegalArgumentException) {
                notifyTxt.postValue("Please enter all fields")
            } catch (e: Exception) {
                notifyTxt.postValue("Username or password is not correct")
            }
        }
    }
}