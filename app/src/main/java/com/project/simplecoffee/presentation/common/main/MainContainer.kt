package com.project.simplecoffee.presentation.common.main

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment

interface MainContainer {
    fun onSignIn()
    fun loadFragment(fragment: Fragment, isAddToBackStack: Boolean = false)
    fun loadDrawable(id: Int): Drawable
    fun backToPreviousFragment()
    fun showMessage(message: String)
    fun toggleNavigationBottom(hide: Boolean = true)
}