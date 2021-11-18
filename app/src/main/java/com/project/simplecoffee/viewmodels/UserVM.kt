package com.project.simplecoffee.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.domain.repository.IUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class UserVM @Inject constructor(
    private val userRepo: IUserRepo,
) : ViewModel(), CoroutineScope{
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var _user = MutableLiveData<FirebaseUser>().apply {
        postValue(userRepo.getCurrentUser())
    }
    val user: LiveData<FirebaseUser> get() = _user

    // For databinding
    val email = MutableLiveData<String>()
    val pwd = MutableLiveData<String>()
    val confirmPwd = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val dob = MutableLiveData<Date>()
    val notifyTxt = MutableLiveData<String>()

    internal val btnSignInVisibility = MutableLiveData(View.VISIBLE)

    fun onSignUpClick() {
        val inputMail = email.value ?: ""
        val inputPwd = pwd.value ?: ""
        val inputConfirmPwd = confirmPwd.value ?: ""
        val inputFirstName = firstName.value ?: ""
        val inputLastName = lastName.value ?: ""
        val inputGender = lastName.value ?: ""
        val inputDOB = dob.value

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (inputFirstName.isEmpty() ||
                    inputLastName.isEmpty() ||
                    inputConfirmPwd.isEmpty() ||
                    inputGender.isEmpty() ||
                    inputDOB == null
                ) {
                    throw IllegalArgumentException()
                }

                if (inputPwd == inputConfirmPwd) {
                    userRepo.signUp(inputMail, inputPwd)

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
                _user.postValue(userRepo.signIn(inputMail, inputPWD).data)
                notifyTxt.postValue("Success to sign in")
            } catch (e: IllegalArgumentException) {
                notifyTxt.postValue("Please enter all fields")
            } catch (e: Exception) {
                notifyTxt.postValue("Username or password is not correct")
            }
        }
    }

}