package com.project.simplecoffee.presentation.order

import android.app.DatePickerDialog
import android.view.View
import androidx.lifecycle.*
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.usecase.order.GetDrinkDetailUseCase
import com.project.simplecoffee.domain.usecase.order.GetOrderByStatusUseCase
import com.project.simplecoffee.domain.usecase.order.GetRevenueFromToUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.CalendarHelper
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.common.round
import com.project.simplecoffee.utils.common.toCustomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject


class RevenueVM @Inject constructor(
    val container: MainContainer,
    private val getRevenueFromToUseCase: GetRevenueFromToUseCase,
    private val getOrderByStatusUseCase: GetOrderByStatusUseCase,
    private val getDrinkDetailUseCase: GetDrinkDetailUseCase
) : OrderVM(container) {
    // Live Data for binding
    private val fromDate = MutableLiveData(LocalDate.now())
    private val toDate = MutableLiveData(LocalDate.now())

    private val _inTotal = MutableLiveData("")
    private val _amountOrder = MutableLiveData("")
    private val _quantityDrink = MutableLiveData("")
    private val _bestSelling = MutableLiveData("")
    private val _loadingSuccessView = MutableLiveData(View.GONE)

    val fromDateStr = Transformations.map(fromDate) { info -> info.toCustomString() }
    val toDateStr = Transformations.map(toDate) { info -> info.toCustomString() }
    val inTotal: LiveData<String>
        get() = _inTotal
    val amountOrder: LiveData<String>
        get() = _amountOrder
    val quantityDrink: LiveData<String>
        get() = _quantityDrink
    val bestSelling: LiveData<String>
        get() = _bestSelling
    val loadingSuccessView: LiveData<Int>
        get() = _loadingSuccessView

    // Date Picker Listener
    val mFromDateListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            fromDate.value = LocalDate.of(year, month + 1, dayOfMonth)
        }

    val mToDateListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            toDate.value = LocalDate.of(year, month + 1, dayOfMonth)
        }

    val mThisDateListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            fromDate.value = LocalDate.of(year, month + 1, dayOfMonth)
            toDate.value = LocalDate.of(year, month + 1, dayOfMonth)
            setData()
        }

    init {
        container.toggleNavigationBottom()
        setData()
    }

    override suspend fun getOrder() = getRevenueFromToUseCase(fromDate.value!!, toDate.value!!)

    fun onTodayClick() {
        fromDate.value = LocalDate.now()
        toDate.value = LocalDate.now()
        setData()
    }

    fun onThisWeekClick() {
        val pair = CalendarHelper.getThisWeek()
        fromDate.value = pair.first
        toDate.value = pair.second
        setData()
    }

    fun onThisMonthClick() {
        val pair = CalendarHelper.getThisMonth()
        fromDate.value = pair.first
        toDate.value = pair.second
        setData()
    }

    fun onThisYearClick() {
        val pair = CalendarHelper.getThisYear()
        fromDate.value = pair.first
        toDate.value = pair.second
        setData()
    }

    fun onGetListOrderClick() {
        setData()
    }

    private fun setData() = viewModelScope.launch {
        getListOrder()
        updateFooter()
    }

    private fun updateFooter() = viewModelScope.launch {
        _loadingSuccessView.value = View.VISIBLE
        _quantityDrink.value = ""
        _amountOrder.value = ""
        _bestSelling.value = ""
        _inTotal.value = ""

        getOrderByStatusUseCase(
            fromDate.value!!,
            toDate.value!!,
            OrderStatus.Success
        ).data?.apply {
            val listDrinkOrder = map { order -> order.drink }
            _quantityDrink.postValue(getDrinkQuantity(listDrinkOrder).toString())
            _amountOrder.postValue(size.toString())
            _bestSelling.postValue(getBestSellingDrink(listDrinkOrder))
            val total = sumOf { it.total!! }.round(2).toString()
            _inTotal.postValue("\$$total")
        } ?: run {
            _quantityDrink.postValue("0")
            _amountOrder.postValue("0")
            _bestSelling.postValue("None")
            _inTotal.postValue("$0")
        }
        _loadingSuccessView.postValue(View.GONE)
    }

    private fun getDrinkQuantity(listDrinkOrder: List<List<OrderItem>?>) =
        listDrinkOrder.sumOf { orderItemList ->
            orderItemList?.run { sumOf { orderItem -> orderItem.quantity } } ?: 0
        }

    private suspend fun getBestSellingDrink(listDrinkOrder: List<List<OrderItem>?>) =
        withContext(viewModelScope.coroutineContext + Dispatchers.Default) {
            val id = listDrinkOrder.flatMap {
                it!!.asIterable()
            }.groupingBy { it.drink.id }
                .aggregate { _, accumulator: OrderItem?, element: OrderItem, _ ->
                    accumulator?.apply {
                        quantity += element.quantity
                    } ?: element
                }.maxByOrNull {
                    it.value.quantity
                }?.key
            id?.run {
                getDrinkDetailUseCase(id).data?.run {
                    name
                } ?: "None"
            } ?: "None"
        }
}