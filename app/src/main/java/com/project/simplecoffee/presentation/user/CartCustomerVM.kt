package com.project.simplecoffee.presentation.user

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.user.ClearCartUseCase
import com.project.simplecoffee.domain.usecase.user.GetCartUserUseCase
import com.project.simplecoffee.domain.usecase.user.MakeOrderUseCase
import com.project.simplecoffee.domain.usecase.user.SaveCartUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartCustomerVM @Inject constructor(
    private val container: MainContainer,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getCartUserUseCase: GetCartUserUseCase,
    private val saveCartUseCase: SaveCartUseCase,
    private val makeOrderUseCase: MakeOrderUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : CartVM(
    container, getCurrentUserUseCase, getCartUserUseCase,
    makeOrderUseCase, clearCartUseCase, saveCartUseCase
) {
    private val _emptyContact = MutableLiveData(View.VISIBLE)
    private val _notEmptyContact = MutableLiveData(View.GONE)
    private val _contact = MutableLiveData<Contact>()

    val emptyContact: LiveData<Int>
        get() = _emptyContact
    val notEmptyContact: LiveData<Int>
        get() = _notEmptyContact
    val name = Transformations.map(_contact) { contact -> contact.name }
    val address = Transformations.map(_contact) { contact -> contact.address }
    val phone = Transformations.map(_contact) { contact -> contact.phone }

    init {
        loadCart()
    }

    override fun updateData() {
        // Update contact
        cart?.contact?.let {
            _contact.value = it
            _emptyContact.value = View.GONE
            _notEmptyContact.value = View.VISIBLE
        }
        super.updateData()
    }

    fun onContactClick() = viewModelScope.launch {
        container.loadFragment(AllMainFragment.ContactSelect.createFragment(), true)
    }

    override fun onOrderClick() {
        _loadingOrderVisible.value = View.INVISIBLE
        if (cart?.contact == null)
            container.showMessage(ErrorConst.ERROR_NOT_SELECT_CONTACT)
        else
            super.onOrderClick()
    }
}