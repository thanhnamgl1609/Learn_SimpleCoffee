package com.project.simplecoffee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.simplecoffee.data.repository.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepo
) : ViewModel() {
    private val _loggedStatus = MutableLiveData<Boolean>()
    val loggedStatus : LiveData<Boolean> get() = _loggedStatus


    init {
        _loggedStatus.postValue(repository.logIn())
    }

    fun signOut() {
        repository.signOut()
        _loggedStatus.postValue(false)
    }

    fun checkLogInStatus() {
        _loggedStatus.postValue(repository.logIn())
    }
}