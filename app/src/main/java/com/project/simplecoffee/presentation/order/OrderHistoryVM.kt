package com.project.simplecoffee.presentation.order

import android.app.DatePickerDialog
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.usecase.order.GetCustomerOrderHistoryUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.CalendarHelper
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class OrderHistoryVM @Inject constructor(
    private val container: MainContainer,
    private val getCustomerOrderHistoryUseCase: GetCustomerOrderHistoryUseCase
) : ViewModel() {
    private val _liveListOrderItemVM = MutableLiveData<List<OrderItemVM>>()
    private val _todayClicked = MutableLiveData(false)
    private val fromDate = MutableLiveData(LocalDate.now())
    private val toDate = MutableLiveData(LocalDate.now())


    val liveListOrderItemVM: LiveData<List<OrderItemVM>>
        get() = _liveListOrderItemVM
    val todayClicked: LiveData<Boolean>
        get() = _todayClicked
    val mThisDateListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            fromDate.value = LocalDate.of(year, month + 1, dayOfMonth)
            toDate.value = LocalDate.of(year, month + 1, dayOfMonth)
            getListOrder()
        }

    init {
        onAllClick()
    }

    fun onTodayClick() {
        fromDate.value = LocalDate.now()
        toDate.value = LocalDate.now()
        _todayClicked.value = true
        getListOrder()
    }

    fun onThisWeekClick() {
        val pair = CalendarHelper.getThisWeek()
        fromDate.value = pair.first
        toDate.value = pair.second
        _todayClicked.value = false
        getListOrder()
    }

    fun onThisMonthClick() {
        val pair = CalendarHelper.getThisMonth()
        fromDate.value = pair.first
        toDate.value = pair.second
        getListOrder()
    }

    fun onThisYearClick() {
        val pair = CalendarHelper.getThisYear()
        fromDate.value = pair.first
        toDate.value = pair.second
        getListOrder()
    }

    fun onAllClick() = viewModelScope.launch {
        handleResult(getCustomerOrderHistoryUseCase())
    }

    private fun getListOrder() =
        viewModelScope.launch {
            handleResult(getCustomerOrderHistoryUseCase(fromDate.value!!, toDate.value!!))
        }

    private fun handleResult(result: Resource<List<Order>?>) {
        when (result) {
            is Resource.OnSuccess -> {
                val listOrderItemVM = mutableListOf<OrderItemVM>()
                result.data?.forEach { order ->
                    listOrderItemVM.add(
                        OrderItemVM(
                            container,
                            order
                        )
                    )
                }
                _liveListOrderItemVM.value = listOrderItemVM
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }
}