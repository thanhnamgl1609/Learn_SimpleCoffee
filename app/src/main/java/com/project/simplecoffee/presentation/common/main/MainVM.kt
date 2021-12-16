package com.project.simplecoffee.presentation.common.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainVM @Inject constructor(
    val container: MainContainer,
    val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private var currFragment: AllMainFragment? = null

    fun loadFragment(frag: AllMainFragment) = viewModelScope.launch {
        if (currFragment != frag) {
            currFragment = when (frag) {
                is AllMainFragment.CurrentOrder -> {
                    if (getCurrentUserUseCase() != null) {
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