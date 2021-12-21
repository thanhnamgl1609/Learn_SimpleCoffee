package com.project.simplecoffee.presentation.user

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.user.ClearCartUseCase
import com.project.simplecoffee.domain.usecase.user.GetCartUserUseCase
import com.project.simplecoffee.domain.usecase.user.MakeOrderUseCase
import com.project.simplecoffee.domain.usecase.user.SaveCartUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class CartVM constructor(
    private val container: MainContainer,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getCartUserUseCase: GetCartUserUseCase,
    private val makeOrderUseCase: MakeOrderUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val saveCartUseCase: SaveCartUseCase
) : ViewModel() {
    private val _total = MutableLiveData("0")
    private val _liveListCartItemVM = MutableLiveData<List<CartItemVM>>(emptyList())
    private val _loadingVisible = MutableLiveData(View.GONE)
    private val _emptyTextVisible = MutableLiveData(View.GONE)
    protected val _loadingOrderVisible = MutableLiveData(View.VISIBLE)
    var saved = true

    val total: LiveData<String>
        get() = _total
    val liveListCartItemVM: LiveData<List<CartItemVM>>
        get() = _liveListCartItemVM
    val loadingVisible: LiveData<Int>
        get() = _loadingVisible
    val emptyTextVisible: LiveData<Int>
        get() = _emptyTextVisible
    val loadingOrderVisible: LiveData<Int>
        get() = _loadingOrderVisible

    protected var cart: Cart? = null

    fun loadCart() = viewModelScope.launch {
        beforeLoading()
        handleCartResult(getCartUserUseCase())
        afterLoading()
    }

    private fun beforeLoading() {
        _loadingVisible.value = View.VISIBLE
        _emptyTextVisible.value = View.GONE
    }

    private fun afterLoading() {
        _loadingVisible.value = View.GONE
        if (_liveListCartItemVM.value!!.isEmpty())
            _emptyTextVisible.value = View.VISIBLE
    }

    protected open fun updateData() {
        createListView()
        updateTotal()
    }

    fun onClearAllClick() {
        viewModelScope.launch {
            handleCartResult(clearCartUseCase())
        }
    }

    open fun onOrderClick() {
        viewModelScope.launch {
            when (val result = makeOrderUseCase(cart)) {
                is Resource.OnSuccess -> {
                    saved = false
                    container.loadFragment(
                        AllMainFragment.CurrentOrder.createFragment(
                            getCurrentUserUseCase()?.role
                        )
                    )
                }
                is Resource.OnFailure -> {
                    container.showMessage(result.message.toString())
                }
            }
            _loadingOrderVisible.value = View.VISIBLE
        }
    }

    private fun handleCartResult(result: Resource<Cart?>) {
        when (result) {
            is Resource.OnSuccess -> {
                cart = result.data
                updateData()
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    private fun updateTotal() {
        _total.value = (cart?.run {
            items?.sumOf { orderItem ->
                (orderItem.drink.price!! * orderItem.quantity)
            } ?: 0
        } ?: 0).toString()
    }

    fun increase(orderItem: OrderItem): Boolean {
        orderItem.apply {
            orderItem.drink.stock?.let {
                if (it == quantity) {
                    container.showMessage("Out of stock")
                    return false
                }
                quantity += 1
            } ?: apply {
                quantity += 1
            }
        }
        updateTotal()
        return true
    }

    fun decrease(orderItem: OrderItem) {
        orderItem.apply {
            if (quantity == 1) {
                cart!!.items!!.remove(this)
                createListView()
            } else
                quantity -= 1
        }
        updateTotal()
    }

    private fun createListView() {
        val listCartItemVM = mutableListOf<CartItemVM>()
        cart!!.items!!.forEach { item ->
            listCartItemVM.add(CartItemVM(this@CartVM, container, item))
        }
        _liveListCartItemVM.value = listCartItemVM
    }

    open fun save() {
        viewModelScope.launch {
            if (saved) {
                val result = saveCartUseCase(cart)
                if (result is Resource.OnFailure)
                    container.showMessage(result.message.toString())
            }
        }
    }
}