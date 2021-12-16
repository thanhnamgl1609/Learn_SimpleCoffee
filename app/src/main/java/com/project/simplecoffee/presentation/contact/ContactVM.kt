package com.project.simplecoffee.presentation.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.domain.usecase.user.GetAllContactUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactVM @Inject constructor(
    private val container: MainContainer,
    private val getAllContactUseCase: GetAllContactUseCase
) : ViewModel() {
    private val _listContactItemVM = MutableLiveData<List<ContactItemVM>>()
    val listContactItemVM: LiveData<List<ContactItemVM>>
        get() = _listContactItemVM

    fun refresh() {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        when (val result = getAllContactUseCase()) {
            is Resource.OnSuccess -> {
                onLoadSuccess(result.data!!)
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    private fun onLoadSuccess(result: List<Contact>) {
        val listContact = mutableListOf<ContactItemVM>()
        for (contact in result) {
            listContact.add(ContactItemVM(this, contact))
        }
        _listContactItemVM.postValue(listContact)
    }

    fun onAddNewContactClick() {
        container.loadFragment(
            AllMainFragment.ContactDetailFragment.createFragment(),
            true
        )
    }

    fun onItemClick(contact: Contact) {
        container.loadFragment(
            AllMainFragment.ContactDetailFragment.createFragment(contact),
            true
        )
    }
}