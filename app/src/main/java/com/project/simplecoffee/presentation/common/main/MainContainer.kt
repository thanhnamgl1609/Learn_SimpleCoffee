package com.project.simplecoffee.presentation.common.main

import androidx.fragment.app.Fragment

interface MainContainer {
    fun onSignIn()
    fun loadFragment(fragment: Fragment, isAddToBackStack: Boolean = false)
    fun showMessage(message: String)
    fun toggleNavigationBottom(hide: Boolean = true)
}