package com.project.simplecoffee.presentation.order

import android.view.View
import androidx.lifecycle.*
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.domain.usecase.order.GetOrderByIDUseCase
import com.project.simplecoffee.domain.usecase.user.GetUserByEmailUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.presentation.inventory.OrderItemDetailVM
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.common.toCustomString
import kotlinx.coroutines.launch
import javax.inject.Inject

open class OrderDetailVM @Inject constructor(
    private val container: MainContainer,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val getOrderByIDUseCase: GetOrderByIDUseCase
) : ViewModel() {
    private val _listDrink = MutableLiveData<List<OrderItemDetailVM>>()

    protected val _id = MutableLiveData("")
    protected val _name = MutableLiveData("")
    protected val _email = MutableLiveData("")
    protected val _date = MutableLiveData("")
    protected val _status = MutableLiveData("")
    protected val _time = MutableLiveData("")
    protected val _loadingVisible = MutableLiveData(View.VISIBLE)
    protected val _loadingItemsVisible = MutableLiveData(View.VISIBLE)

    val id: LiveData<String>
        get() = _id
    val name: LiveData<String>
        get() = _name
    val email: LiveData<String>
        get() = _email
    val date: LiveData<String>
        get() = _date
    val status: LiveData<String>
        get() = _status
    val time: LiveData<String>
        get() = _time
    val loadingVisible: LiveData<Int>
        get() = _loadingVisible
    val loadingItemsVisible: LiveData<Int>
        get() = _loadingItemsVisible
    val listDrink: LiveData<List<OrderItemDetailVM>>
        get() = _listDrink

    fun setOrder(orderID: String) {
        viewModelScope.launch {
            _loadingItemsVisible.value = View.VISIBLE
            _loadingVisible.value = View.VISIBLE
            handleOrderResult(getOrderByIDUseCase(orderID))
        }
    }

    private fun handleOrderResult(result: Resource<Order?>) {
        when (result) {
            is Resource.OnSuccess -> {
                viewModelScope.launch {
                    onOrderSuccess(result.data!!)
                }
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    protected open suspend fun onOrderSuccess(order: Order) {
        setOrderView(order)
        setListDrink(order)
    }

    private suspend fun setOrderView(order: Order) {
        _id.postValue(order.id)
        _email.postValue(order.email ?: "")
        _date.postValue(order.createdAt!!.toLocalDate().toCustomString())
        _status.postValue(order.status!!.status)
        _time.postValue(order.createdAt.toLocalTime().toCustomString())
        _name.postValue(
            getUserByEmailUseCase(order.email)
                .data?.run { "$firstname $lastname" }
                ?: "")
        _loadingVisible.value = View.GONE
    }

    private fun setListDrink(order: Order) {
        val listItems = mutableListOf<OrderItemDetailVM>()
        order.drink?.forEach { orderItem ->
            listItems.add(OrderItemDetailVM(orderItem))
        }
        _listDrink.value = listItems
        _loadingItemsVisible.value = View.GONE
    }
}