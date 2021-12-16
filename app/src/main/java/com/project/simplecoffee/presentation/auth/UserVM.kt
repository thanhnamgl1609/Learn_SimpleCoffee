package com.project.simplecoffee.presentation.auth

import android.app.DatePickerDialog
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.common.toCustomString
import com.project.simplecoffee.domain.model.details.Gender
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.auth.SignInUseCase
import com.project.simplecoffee.domain.usecase.auth.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class UserVM @Inject constructor(
    private val container: AuthContainer,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private var dob = MutableLiveData(LocalDate.now())
    val mDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            dob.value = LocalDate.of(year, month + 1, dayOfMonth)
        }

    // For databinding
    val inputEmail = MutableLiveData<String>()
    val inputPWD = MutableLiveData<String>()
    val inputConfirmPWD = MutableLiveData<String>()
    val inputFirstName = MutableLiveData<String>()
    val inputLastName = MutableLiveData<String>()
    val inputGender = MutableLiveData<Int>()
    val inputDoB = Transformations.map(dob) { info -> info.toCustomString() }

    val btnVisible = MutableLiveData(View.VISIBLE)

    init {
        viewModelScope.launch {
            if (getCurrentUserUseCase() != null)
                container.finishActivity()
        }
    }

    fun onSignUpClick() = viewModelScope.launch() {
        btnVisible.postValue(View.GONE)
        val email = inputEmail.value
        val pwd = inputPWD.value
        val confirmPWD = inputConfirmPWD.value
        val firstName = inputFirstName.value
        val lastName = inputLastName.value
        val gender = inputGender.value

        handleResult(
            signUpUseCase(
                email,
                pwd,
                confirmPWD,
                firstName,
                lastName,
                gender == Gender.Male.index,
                dob.value!!
            )
        )
    }

    // View binding
    fun onSignInClick() = viewModelScope.launch() {
        btnVisible.postValue(View.GONE)
        handleResult(signInUseCase(inputEmail.value.toString(), inputPWD.value.toString()))
    }

    private fun handleResult(result: Resource<User?>) {
        when (result) {
            is Resource.OnSuccess -> {
                container.finishActivity()
            }
            is Resource.OnFailure -> {
                container.showError(result.message!!)
                btnVisible.postValue(View.VISIBLE)
            }
        }
    }
}