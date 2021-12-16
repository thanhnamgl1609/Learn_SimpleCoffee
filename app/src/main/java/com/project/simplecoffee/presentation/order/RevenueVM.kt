package com.project.simplecoffee.presentation.order

import android.app.DatePickerDialog
import androidx.lifecycle.*
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.domain.usecase.order.GetDrinkDetailUseCase
import com.project.simplecoffee.domain.usecase.order.GetRevenueByStatusUseCase
import com.project.simplecoffee.domain.usecase.order.GetRevenueFromToUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.CalendarHelper
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.common.toCustomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject


class RevenueVM @Inject constructor(
    val container: MainContainer,
    private val getRevenueFromToUseCase: GetRevenueFromToUseCase,
    private val getRevenueByStatusUseCase: GetRevenueByStatusUseCase,
    private val getDrinkDetailUseCase: GetDrinkDetailUseCase
) : ViewModel() {
    // Live Data for binding
    private val fromDate = MutableLiveData(LocalDate.now())
    private val toDate = MutableLiveData(LocalDate.now())
    private val _liveListOrderItemVM = MutableLiveData<List<OrderItemVM>>()
    private val _inTotal = MutableLiveData("0")
    private val _amountOrder = MutableLiveData("0")
    private val _quantityDrink = MutableLiveData("0")
    private val _bestSelling = MutableLiveData<String>()

    val fromDateStr = Transformations.map(fromDate) { info -> info.toCustomString() }
    val toDateStr = Transformations.map(toDate) { info -> info.toCustomString() }
    val liveListOrderItemVM: LiveData<List<OrderItemVM>>
        get() = _liveListOrderItemVM
    val inTotal: LiveData<String>
        get() = _inTotal
    val amountOrder: LiveData<String>
        get() = _amountOrder
    val quantityDrink: LiveData<String>
        get() = _quantityDrink
    val bestSelling: LiveData<String>
        get() = _bestSelling

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
            getListOrder()
        }

    init {
        container.toggleNavigationBottom()
        getListOrder()
    }

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

    fun onGetListOrderClick() {
        getListOrder()
    }

    private fun getListOrder() =
        viewModelScope.launch {
            handleResult(getRevenueFromToUseCase(fromDate.value!!, toDate.value!!))
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
                updateFooter()
                _liveListOrderItemVM.postValue(listOrderItemVM)
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    private fun updateFooter() = viewModelScope.launch {
        getRevenueByStatusUseCase(
            fromDate.value!!,
            toDate.value!!,
            OrderStatus.Success
        ).data?.apply {
            val listDrinkOrder = map { order -> order.drink }
            _quantityDrink.postValue(getDrinkQuantity(listDrinkOrder).toString())
            _amountOrder.postValue(size.toString())
            _bestSelling.postValue(getBestSellingDrink(listDrinkOrder))
            _inTotal.postValue(sumOf { it.total!! }.toString())
        } ?: run {
            _quantityDrink.postValue("0")
            _amountOrder.postValue("0")
            _bestSelling.postValue("")
            _inTotal.postValue("0")
        }
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
                } ?: ""
            } ?: ""
        }
}