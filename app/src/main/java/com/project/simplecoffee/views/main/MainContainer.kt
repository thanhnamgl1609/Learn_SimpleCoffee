package com.project.simplecoffee.views.main

import androidx.fragment.app.Fragment

interface MainContainer {
    fun onSignIn()
    fun loadFragment(fragment: Fragment)
    fun showMessage(message: String)
}