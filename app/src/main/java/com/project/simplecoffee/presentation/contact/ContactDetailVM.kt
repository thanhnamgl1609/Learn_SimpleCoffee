package com.project.simplecoffee.presentation.contact

import android.view.View
import androidx.lifecycle.*
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.SuccessConst
import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.domain.usecase.user.CreateContactUseCase
import com.project.simplecoffee.domain.usecase.user.DeleteContactUseCase
import com.project.simplecoffee.domain.usecase.user.UpdateContactUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactDetailVM @Inject constructor(
    private val container: MainContainer,
    private val createContactUseCase: CreateContactUseCase,
    private val updateContactUseCase: UpdateContactUseCase,
    private val deleteContactUseCase: DeleteContactUseCase
) : ViewModel() {
    private val _contact = MutableLiveData<Contact>()

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
    val removeVisibility = MutableLiveData(View.GONE)

    fun onDoneClick() = viewModelScope.launch {
        if (_contact.value == null) {
            handleResult(
                createContactUseCase(
                    name.value,
                    address.value,
                    phone.value
                )
            )
        } else {
            handleResult(
                updateContactUseCase(
                    _contact.value!!.id,
                    name.value,
                    address.value,
                    phone.value
                ), true
            )
        }
    }

    fun onRemoveClick() = viewModelScope.launch {
        handleResult(deleteContactUseCase(_contact.value!!.id))
    }

    private fun handleResult(result: Resource<Contact?>, isUpdate: Boolean = false) {
        when (result) {
            is Resource.OnSuccess -> {
                if (!isUpdate)
                    container.loadFragment(AllMainFragment.Contact.createFragment())
                else {
                    result.data!!.apply {
                        _contact.postValue(this)
                        container.showMessage(SuccessConst.SUCCESS_UPDATE)
                    }
                    container.loadFragment(AllMainFragment.Contact.createFragment())
                }
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    fun setContact(contact: Contact) {
        _contact.postValue(contact)
        removeVisibility.postValue(View.VISIBLE)
    }
}