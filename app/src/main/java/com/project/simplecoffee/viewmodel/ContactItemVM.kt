package com.project.simplecoffee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.models.Contact
import com.project.simplecoffee.domain.repository.IContactRepo

class ContactItemVM constructor(
    val contactVM: ContactVM,
    val contact: Contact
) : ViewModel(), ItemVM {
    override val viewType: Int
        get() = R.layout.contact_row_item

    private val _name = MutableLiveData<String>(contact.name)
    private val _address = MutableLiveData<String>(contact.address)
    private val _phone = MutableLiveData<String>(contact.phone)

    val name: LiveData<String>
        get() = _name
    val address: LiveData<String>
        get() = _address
    val phone: LiveData<String>
        get() = _phone

    fun onItemClick() {
        contactVM.onItemClick(contact)
    }
}