package com.project.simplecoffee.presentation.order

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.usecase.order.AcceptForCancellingUseCase
import com.project.simplecoffee.domain.usecase.order.GetOrderByIDUseCase
import com.project.simplecoffee.domain.usecase.order.NextStatusOrderProcessingUseCase
import com.project.simplecoffee.domain.usecase.user.GetUserByEmailUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailStaffVM @Inject constructor(
    private val container: MainContainer,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val getOrderByIDUseCase: GetOrderByIDUseCase,
    private val acceptForCancellingUseCase: AcceptForCancellingUseCase,
    private val nextStatusOrderProcessingUseCase: NextStatusOrderProcessingUseCase
) : OrderDetailVM(
    container,
    getUserByEmailUseCase,
    getOrderByIDUseCase
) {
    private val _nextStatus = MutableLiveData("")
    private val _acceptBtnVisible = MutableLiveData(View.GONE)
    private val _updateBtnVisible = MutableLiveData(View.GONE)

    val acceptBtnVisible: LiveData<Int>
        get() = _acceptBtnVisible

    val updateBtnVisible: LiveData<Int>
        get() = _updateBtnVisible

    val nextStatus: LiveData<String>
        get() = _nextStatus

    fun acceptForCancelling() {
        viewModelScope.launch {
            _acceptBtnVisible.value = View.INVISIBLE
            handleOrderStatusResult(acceptForCancellingUseCase(_id.value))
        }
    }

    fun updateStatus() {
        viewModelScope.launch {
            _updateBtnVisible.value = View.INVISIBLE
            handleOrderStatusResult(nextStatusOrderProcessingUseCase(_id.value))
        }
    }

    private fun handleOrderStatusResult(result: Resource<Order?>) {
        if (result is Resource.OnFailure) {
            container.showMessage(container.toString())
        }
        result.data?.let { order ->
            updateNextButton(order)
            updateRequestButton(order)
            _status.value = order.status!!.status
        }
    }

    private fun updateNextButton(order: Order) {
        val currentStatus = order.status!!
        val nextStatus = currentStatus.next()
        _updateBtnVisible.value =
            if (currentStatus is OrderStatus.Cancelled
                || currentStatus is OrderStatus.Success
            )
                View.GONE else View.VISIBLE
        _nextStatus.value = nextStatus.status
    }

    private fun updateRequestButton(order: Order) {
        _acceptBtnVisible.value =
            if (order.status is OrderStatus.RequestCancel)
                View.VISIBLE else View.GONE
    }

    override suspend fun onOrderSuccess(order: Order) {
        super.onOrderSuccess(order)
        updateRequestButton(order)
        updateNextButton(order)
    }
}