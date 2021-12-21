package com.project.simplecoffee.presentation.order

import android.app.DatePickerDialog
import android.util.Log
import android.view.View
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
) : OrderVM(container) {
    private val fromDate = MutableLiveData(LocalDate.now())
    private val toDate = MutableLiveData(LocalDate.now())

    val mThisDateListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            fromDate.value = LocalDate.of(year, month + 1, dayOfMonth)
            toDate.value = LocalDate.of(year, month + 1, dayOfMonth)
            getListOrder()
        }

    init {
        onAllClick()
    }

    override suspend fun getOrder() =
        getCustomerOrderHistoryUseCase(fromDate.value!!, toDate.value!!)

    fun onTodayClick() {
        fromDate.value = LocalDate.now()
        toDate.value = LocalDate.now()
        getListOrder()
    }

    fun onThisWeekClick() {
        val pair = CalendarHelper.getThisWeek()
        fromDate.value = pair.first
        toDate.value = pair.second
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
        beforeLoading()
        handleResult(getCustomerOrderHistoryUseCase())
        afterLoading()
    }
}