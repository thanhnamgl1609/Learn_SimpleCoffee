package com.project.simplecoffee.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.constant.SuccessConst
import com.project.simplecoffee.domain.models.Contact
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.main.AllMainFragment
import com.project.simplecoffee.views.main.MainContainer
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactDetailVM @Inject constructor(
    val container: MainContainer,
    val userRepo: IUserRepo
) : ViewModel() {
    private val _contact = MutableLiveData<Contact>()
    private val contactRepo = userRepo.getContactRepo().data?.apply {
        current?.let { contact -> _contact.value = contact }
    }

    val name = MediatorLiveData<String>().apply {
        addSource(_contact) { info ->
            value = info?.name
        }
    }
    val address = MediatorLiveData<String>().apply {
        addSource(_contact) { info ->
            value = info?.address
        }
    }
    val phone = MediatorLiveData<String>().apply {
        addSource(_contact) { info ->
            value = info?.phone
        }
    }
    val removeVisibility = MutableLiveData<Int>().apply {
        if (contactRepo?.current == null)
            value = View.GONE
        else
            value = View.VISIBLE

    }

    fun onDoneClick() = viewModelScope.launch {
        val iName = name.value
        val iAddress = address.value
        val iPhone = phone.value
        if (_contact.value == null) {
            when (val result = contactRepo!!.createContact(iName, iAddress, iPhone)) {
                is Resource.OnSuccess -> {
                    contactRepo.current = result.data
                    container.loadFragment(AllMainFragment.Contact.createFragment())
                }
                is Resource.OnFailure -> {
                    container.showMessage(result.message.toString())
                }
            }
        } else {
            when (val result = contactRepo!!.updateContact(iName, iAddress, iPhone)) {
                is Resource.OnSuccess -> {
                    result.data!!.apply {
                        contactRepo.current = this
                        _contact.postValue(this)
                        container.showMessage(SuccessConst.SUCCESS_UPDATE)
                    }
                }
                is Resource.OnFailure -> {
                    container.showMessage(result.message.toString())
                }
            }
        }
    }

    fun onRemoveClick() = viewModelScope.launch {
        when (val result = contactRepo!!.deleteContact()) {
            is Resource.OnSuccess -> {
                container.loadFragment(AllMainFragment.Contact.createFragment())
                contactRepo.current = null
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    fun clear() {
        contactRepo!!.current = null
    }
}