package com.project.simplecoffee.presentation.order

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.usecase.order.AcceptForCancellingUseCase
import com.project.simplecoffee.domain.usecase.order.GetOrderByIDUseCase
import com.project.simplecoffee.domain.usecase.order.NextStatusOrderProcessingUseCase
import com.project.simplecoffee.domain.usecase.user.GetUserByEmailUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailTableVM @Inject constructor(
    private val container: MainContainer,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val getOrderByIDUseCase: GetOrderByIDUseCase,
    private val markTableAsEmptyUseCase: MarkTableAsEmptyUseCase
) : OrderDetailVM(
    container,
    getUserByEmailUseCase,
    getOrderByIDUseCase
) {
    private val _btnVisible = MutableLiveData(View.VISIBLE)

    val btnVisible: LiveData<Int>
        get() = _btnVisible

    fun markAsEmpty() {
        viewModelScope.launch {
            _btnVisible.value = View.INVISIBLE
            handleOrderStatusResult(markTableAsEmptyUseCase(_id.value))
        }
    }

    private fun handleOrderStatusResult(result: Resource<Table?>) {
        when (result) {
            is Resource.OnSuccess -> {
                container.backToPreviousFragment()
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
                _btnVisible.value = View.VISIBLE
            }
        }
    }
}