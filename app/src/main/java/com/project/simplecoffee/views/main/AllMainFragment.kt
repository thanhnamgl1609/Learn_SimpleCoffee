package com.project.simplecoffee.views.main

import androidx.fragment.app.Fragment

sealed class AllMainFragment {
    object AccountInfo : AllMainFragment() {
        override fun createFragment(): Fragment {
            return AccountInfoFragment()
        }
    }

    object Setting : AllMainFragment() {
        override fun createFragment(): Fragment {
            return AccountInfoFragment()
        }
    }

    object Menu : AllMainFragment() {
        override fun createFragment(): Fragment {
            return MenuFragment()
        }
    }

    abstract fun createFragment(): Fragment
}