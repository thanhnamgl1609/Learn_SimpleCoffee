package com.project.simplecoffee.presentation.common.main

import androidx.fragment.app.Fragment
import com.project.simplecoffee.presentation.common.setting.SettingFragment
import com.project.simplecoffee.presentation.contact.ContactDetailFragment
import com.project.simplecoffee.presentation.contact.ContactFragment
import com.project.simplecoffee.presentation.inventory.MenuFragment
import com.project.simplecoffee.presentation.order.OrderHistoryFragment
import com.project.simplecoffee.presentation.user.AccountInfoFragment
import com.project.simplecoffee.presentation.order.RevenueFragment
import com.project.simplecoffee.presentation.user.CartCustomerFragment
import com.project.simplecoffee.presentation.user.CartStaffFragment

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
        fun createFragment(contact: com.project.simplecoffee.domain.model.Contact) =
            ContactDetailFragment().apply { this.contact = contact }
    }

    object Revenue : AllMainFragment() {
        override fun createFragment() = RevenueFragment()

    }

    object OrderHistory : AllMainFragment() {
        override fun createFragment() = OrderHistoryFragment()
    }

    object CartStaff : AllMainFragment() {
        override fun createFragment() = CartStaffFragment()
    }

    object CartCustomer : AllMainFragment() {
        override fun createFragment() = CartCustomerFragment()
    }

    abstract fun createFragment(): Fragment
}