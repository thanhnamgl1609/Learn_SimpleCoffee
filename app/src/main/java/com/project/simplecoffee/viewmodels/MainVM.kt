package com.project.simplecoffee.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.data.repository.UserRepo
import com.project.simplecoffee.views.main.AccountInfoFragment
import com.project.simplecoffee.views.main.MainContainer
import com.project.simplecoffee.views.main.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainVM @Inject constructor(
    val container: MainContainer,
    val userRepo: UserRepo
) : ViewModel() {
    fun loadFragment(frag: MainFragment) = viewModelScope.launch {
        if (userRepo.getCurrentUser() != null) {
            withContext(Dispatchers.Main) {
                container.loadFragment(frag.createFragment())
            }
        } else {
            withContext(Dispatchers.Main) {
                container.onSignIn()
            }
        }
    }
}