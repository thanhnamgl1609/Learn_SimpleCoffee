package com.project.simplecoffee.viewmodel

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.view.View
import android.widget.DatePicker
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.common.toLocalDate
import com.project.simplecoffee.domain.models.details.Gender
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.auth.AuthContainer
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class UserVM @Inject constructor(
    private val container: AuthContainer,
    private val userRepo: IUserRepo,
) : ViewModel() {
    private var dob = MutableLiveData(LocalDate.now())
    val mDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            dob.postValue(
                LocalDate.of(year, month + 1, dayOfMonth)
            )
        }

    // For databinding
    val inputEmail = MutableLiveData<String>()
    val inputPWD = MutableLiveData<String>()
    val inputConfirmPWD = MutableLiveData<String>()
    val inputFirstName = MutableLiveData<String>()
    val inputLastName = MutableLiveData<String>()
    val inputGender = MutableLiveData<Int>()
    val inputDoB = Transformations.map(dob) { info -> info.toString() }

    val btnVisible = MutableLiveData(View.VISIBLE)

    init {
        if (userRepo.getCurrentUser() != null)
            container.finishActivity()
    }

    fun onSignUpClick() = viewModelScope.launch() {
        btnVisible.postValue(View.GONE)
        val email = inputEmail.value
        val pwd = inputPWD.value
        val confirmPWD = inputConfirmPWD.value
        val firstName = inputFirstName.value
        val lastName = inputLastName.value
        val gender = inputGender.value

        when (val result = userRepo.signUp(
            email,
            pwd,
            confirmPWD,
            firstName,
            lastName,
            gender == Gender.Male.index,
            dob.value!!
        )) {
            is Resource.OnSuccess -> {
                container.finishActivity()
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
                container.finishActivity()
            }
            is Resource.OnFailure -> {
                container.showError(result.message!!)
                btnVisible.postValue(View.VISIBLE)
            }
        }
    }
}