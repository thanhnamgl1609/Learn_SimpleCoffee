package com.project.simplecoffee.views.main

import androidx.fragment.app.Fragment

sealed class AllMainFragment {
    object CurrentOrder : AllMainFragment() {
        override fun createFragment() = AccountInfoFragment()
    }

    object AccountInfo : AllMainFragment() {
        override fun createFragment() = AccountInfoFragment()
    }

    object Setting : AllMainFragment() {
        override fun createFragment() = SettingFragment()
    }

    object Menu : AllMainFragment() {
        override fun createFragment() = MenuFragment()
    }

    object Contact : AllMainFragment() {
        override fun createFragment() = ContactFragment()
    }

    object ContactDetailFragment : AllMainFragment() {
        override fun createFragment() = ContactDetailFragment()
    }

    object Revenue : AllMainFragment() {
        override fun createFragment(): Fragment = RevenueFragment()

    }

    abstract fun createFragment(): Fragment
}