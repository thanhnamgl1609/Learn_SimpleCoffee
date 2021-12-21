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

    fun loadFragment(frag: AllMainFragment) = viewModelScope.launch {
        when (frag) {
            is AllMainFragment.CurrentOrder -> {
                getCurrentUserUseCase()?.run {
                    container.loadFragment(frag.createFragment(role))
                } ?: run {
                    container.onSignIn()
                }
            }
            else -> {
                container.loadFragment(frag.createFragment())
            }
        }
    }
}