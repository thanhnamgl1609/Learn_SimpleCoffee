package com.project.simplecoffee.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.Contact
import com.project.simplecoffee.domain.repository.IContactRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.main.AllMainFragment
import com.project.simplecoffee.views.main.MainContainer
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactVM @Inject constructor(
    val container: MainContainer,
    val userRepo: IUserRepo
) : ViewModel() {
    private val _listContactItemVM = MutableLiveData<List<ContactItemVM>>()
    private lateinit var contactRepo: IContactRepo
    val listContactItemVM: LiveData<List<ContactItemVM>>
        get() = _listContactItemVM
    var listTest = emptyList<Contact>()

    fun refresh() {
        loadData(this)
    }

    private fun loadData(contactVM: ContactVM) = viewModelScope.launch {
        if (getContactUser()) {
            // Get All Address
            when (val result = contactRepo.getContact()) {
                is Resource.OnSuccess -> {
                    listTest = result.data!!
                    val listContact = mutableListOf<ContactItemVM>()
                    for (contact in result.data) {
                        listContact.add(ContactItemVM(contactVM, contact))
                    }
                    _listContactItemVM.postValue(listContact)
                }
                is Resource.OnFailure -> {
                    container.showMessage(result.message.toString())
                }
            }
        }
    }

    private fun getContactUser(): Boolean {
        return when (val result = userRepo.getContactRepo()) {
            is Resource.OnSuccess -> {
                contactRepo = result.data!!
                true
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
                false
            }
            else -> false
        }
    }

    fun onAddNewContactClick() {
        container.loadFragment(AllMainFragment.ContactDetailFragment.createFragment(), true)
    }

    fun onItemClick(contact: Contact) {
        contactRepo.current = contact
        container.loadFragment(AllMainFragment.ContactDetailFragment.createFragment(), true)
    }
}