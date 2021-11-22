package com.project.simplecoffee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.data.repository.UserRepo
import com.project.simplecoffee.views.main.MainContainer
import com.project.simplecoffee.views.main.AllMainFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainVM @Inject constructor(
    val container: MainContainer,
    val userRepo: UserRepo
) : ViewModel() {
    private var curr_fragment: AllMainFragment? = null

    fun loadFragment(frag: AllMainFragment) = viewModelScope.launch {
        if (curr_fragment != frag) {
            curr_fragment = when (frag) {
                is AllMainFragment.AccountInfo -> {
                    if (userRepo.getCurrentUser() != null) {
                        container.loadFragment(frag.createFragment())
                    } else {
                        container.onSignIn()
                    }
                    frag
                }
                else -> {
                    container.loadFragment(frag.createFragment())
                    frag
                }
            }
        }
    }
}