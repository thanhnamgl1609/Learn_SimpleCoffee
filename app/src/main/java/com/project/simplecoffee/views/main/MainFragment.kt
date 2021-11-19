package com.project.simplecoffee.views.main

import androidx.fragment.app.Fragment

sealed class MainFragment {
    object AccountInfo : MainFragment() {
        override fun createFragment(): Fragment {
            return AccountInfoFragment()
        }
    }

    object Setting : MainFragment() {
        override fun createFragment(): Fragment {
            return AccountInfoFragment()
        }
    }

    abstract fun createFragment() : Fragment
}