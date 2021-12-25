package com.project.simplecoffee.presentation.order

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.usecase.order.GetOrderByIDUseCase
import com.project.simplecoffee.domain.usecase.order.RequestForCancelOrderUseCase
import com.project.simplecoffee.domain.usecase.user.GetUserByEmailUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailCustomerVM @Inject constructor(
    private val container: MainContainer,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val getOrderByIDUseCase: GetOrderByIDUseCase,
    private val isAvailableForCancelOrderUseCase: IsAvailableForCancelOrderUseCase,
    private val requestForCancelOrderUseCase: RequestForCancelOrderUseCase
) : OrderDetailVM(
    container,
    getUserByEmailUseCase,
    getOrderByIDUseCase
) {
    private val _btnEnabled = MutableLiveData(false)
    private val _requestBtn = MutableLiveData(View.VISIBLE)

    val btnEnabled: LiveData<Boolean>
        get() = _btnEnabled
    val requestBtn: LiveData<Int>
        get() = _requestBtn

    fun onCancelRequest() {
        viewModelScope.launch {
            _requestBtn.value = View.INVISIBLE
            handleRequestResult(requestForCancelOrderUseCase(_id.value))
            _btnEnabled.postValue(isAvailableForCancelOrderUseCase(_id.value))
        }
    }

    private fun handleRequestResult(result: Resource<Order?>) {
        when (result) {
            is Resource.OnSuccess -> {
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
        result.data?.apply { _status.value = status!!.status }
        _requestBtn.value = View.VISIBLE
    }

    override suspend fun onOrderSuccess(order: Order) {
        super.onOrderSuccess(order)
        _btnEnabled.postValue(isAvailableForCancelOrderUseCase(order.id))
    }
}