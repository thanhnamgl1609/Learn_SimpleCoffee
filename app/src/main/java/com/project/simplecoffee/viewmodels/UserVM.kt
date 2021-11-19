package com.project.simplecoffee.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.details.Gender
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.auth.AuthContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class UserVM @Inject constructor(
    private val container: AuthContainer,
    private val userRepo: IUserRepo,
) : ViewModel() {

    // For databinding
    val inputEmail = MutableLiveData<String>()
    val inputPWD = MutableLiveData<String>()
    val inputConfirmPWD = MutableLiveData<String>()
    val inputFirstName = MutableLiveData<String>()
    val inputLastName = MutableLiveData<String>()
    val inputGender = MutableLiveData<Int>()
    val inputDoB = MutableLiveData<Date>()

    val btnVisible = MutableLiveData<Int>().apply {
        postValue(View.VISIBLE)
    }

    init {
        if (userRepo.getCurrentUser() != null)
            container.finish()
    }

    fun onSignUpClick() = viewModelScope.launch() {
        btnVisible.postValue(View.GONE)
        val email = inputEmail.value
        val pwd = inputPWD.value
        val confirmPWD = inputConfirmPWD.value
        val firstName = inputFirstName.value
        val lastName = inputLastName.value
        val gender = inputGender.value
        val dob = inputDoB.value!!

        when (val result = userRepo.signUp(
            email,
            pwd,
            confirmPWD,
            firstName,
            lastName,
            gender == Gender.Male.index,
            dob
        )) {
            is Resource.OnSuccess -> {
                container.finish()
            }
            is Resource.OnFailure -> {
                container.showError(result.message!!)
                btnVisible.postValue(View.VISIBLE)
            }
        }
    }

    // View binding
    fun onSignInClick() = viewModelScope.launch() {
        btnVisible.postValue(View.GONE)
        when (val result = userRepo.signIn(
            inputEmail.value ?: "",
            inputPWD.value ?: ""
        )) {
            is Resource.OnSuccess -> {
                container.finish()
            }
            is Resource.OnFailure -> {
                container.showError(result.message!!)
                btnVisible.postValue(View.VISIBLE)
            }
        }

    }
}