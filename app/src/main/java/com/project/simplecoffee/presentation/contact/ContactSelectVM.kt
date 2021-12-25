package com.project.simplecoffee.presentation.contact

import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.domain.usecase.user.GetAllContactUseCase
import com.project.simplecoffee.domain.usecase.user.SetContactInCartUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactSelectVM @Inject constructor(
    private val container: MainContainer,
    private val getAllContactUseCase: GetAllContactUseCase,
    private val setContactInCartUseCase: SetContactInCartUseCase
) : ContactVM(container, getAllContactUseCase) {
    override fun onItemClick(contact: Contact) {
        viewModelScope.launch {
            setContactInCartUseCase(contact)
            container.backToPreviousFragment()
        }
    }
}