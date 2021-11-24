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
            return SettingFragment()
        }
    }

    object Menu : AllMainFragment() {
        override fun createFragment(): Fragment {
            return MenuFragment()
        }
    }

    object Contact : AllMainFragment() {
        override fun createFragment(): Fragment {
            return ContactFragment()
        }
    }

    object ContactDetailFragment : AllMainFragment() {
        override fun createFragment(): Fragment {
            return ContactDetailFragment()
        }
    }

    abstract fun createFragment(): Fragment
}