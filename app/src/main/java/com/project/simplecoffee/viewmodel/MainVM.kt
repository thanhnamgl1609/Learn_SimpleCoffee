package com.project.simplecoffee.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.models.UserInfo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.repository.UserRepo
import com.project.simplecoffee.views.main.MainContainer
import com.project.simplecoffee.views.main.AllMainFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainVM @Inject constructor(
    val container: MainContainer,
    val userRepo: IUserRepo
) : ViewModel() {
    private var curr_fragment: AllMainFragment? = null

    fun loadFragment(frag: AllMainFragment) = viewModelScope.launch {
        if (curr_fragment != frag) {
            curr_fragment = when (frag) {
                is AllMainFragment.CurrentOrder -> {
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