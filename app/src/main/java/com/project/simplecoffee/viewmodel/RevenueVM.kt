package com.project.simplecoffee.viewmodel

import android.app.DatePickerDialog
import android.util.Log
import androidx.lifecycle.*
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.common.toCustomString
import com.project.simplecoffee.common.toTimestamp
import com.project.simplecoffee.domain.models.Order
import com.project.simplecoffee.domain.models.details.OrderStatus
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.main.MainContainer
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject


class RevenueVM @Inject constructor(
    val container: MainContainer,
    val userRepo: IUserRepo,
    private val drinkCategoryRepo: IDrinkCategoryRepo,
    private val drinkRepo: IDrinkRepo
) : ViewModel() {
    // Live Data for binding
    private val fromDate = MutableLiveData(LocalDate.now())
    private val toDate = MutableLiveData(LocalDate.now())
    private val _liveListOrderItemVM = MutableLiveData<List<OrderItemVM>>()
    private val _inTotal = MutableLiveData<String>("0")
    private val _amountOrder = MutableLiveData<String>("0")
    private val _quantityDrink = MutableLiveData<String>("0")
    private val _bestSelling = MutableLiveData<String>()
    private val cal = Calendar.getInstance()
    private val orderRepo = userRepo.getOrderRepo().data!!
    private var listOrder: List<Order>? = null
    private var listSuccess: List<Order>? = null
    private var listDrinkOrder: List<List<MutableMap<String, Any>>?>? = null

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
        getListOrder()
    }

    fun onTodayClick() {
        fromDate.value = LocalDate.now()
        toDate.value = LocalDate.now()
        getListOrder()
    }

    fun onThisWeekClick() {
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        fromDate.value = LocalDate.of(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
        toDate.value = fromDate.value?.plusDays(6)
        getListOrder()
    }

    fun onThisMonthClick() {
        fromDate.value = LocalDate.of(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            1
        )
        toDate.value = fromDate.value?.withDayOfMonth(
            cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
        getListOrder()
    }

    fun onThisYearClick() {
        fromDate.value = LocalDate.of(
            cal.get(Calendar.YEAR),
            1,
            1
        )
        toDate.value = LocalDate.of(
            cal.get(Calendar.YEAR),
            12,
            31
        )
        getListOrder()
    }

    fun onGetListOrderClick() {
        getListOrder()
    }

    private fun getListOrder() {
        val revenueVM = this
        viewModelScope.launch {
            val from = fromDate.value!!.toTimestamp()
            val to = toDate.value!!.atTime(23, 59, 59).toTimestamp()
            when (val result = orderRepo.getOrderFromTo(from, to)) {
                is Resource.OnSuccess -> {
                    listOrder = result.data!!
                    listSuccess =
                        listOrder!!.filter { order -> order.status == OrderStatus.Success.status }
                    listDrinkOrder = listSuccess!!.map { order -> order.drink }
                    getDrinkQuantity()
                    getAmountOfCompleteOrder()
                    getBestSellingDrink()
                    getTotal()
                    val listOrderItemVM = mutableListOf<OrderItemVM>()
                    for (order in listOrder!!) {
                        listOrderItemVM.add(
                            OrderItemVM(
                                container,
                                revenueVM,
                                drinkRepo,
                                drinkCategoryRepo,
                                order
                            )
                        )
                    }
                    _liveListOrderItemVM.postValue(listOrderItemVM)
                }
                is Resource.OnFailure -> {
                    container.showMessage(result.message.toString())
                }
            }
        }
    }

    private fun getTotal() {
        val total = listOrder?.sumOf { it.total!! }
        _inTotal.postValue(
            total?.toString() ?: "0"
        )
    }

    private fun getAmountOfCompleteOrder() {
        _amountOrder.postValue(listSuccess!!.size.toString())
    }

    private fun getDrinkQuantity() {
        val sum = listDrinkOrder!!.sumOf { list ->
            list!!.sumOf { drink -> drink["quantity"].toString().toInt() }
        }
        _quantityDrink.postValue(sum.toString())
    }

    private fun getBestSellingDrink() = viewModelScope.launch {
        val flatten = listDrinkOrder?.flatMap {
            it!!.asIterable()
        }
        val grouping = flatten?.groupingBy { it["drink"] }
            ?.aggregate { _, accumulator: MutableMap<String, Any>?, element: MutableMap<String, Any>, _ ->
                accumulator?.let {
                    it["quantity"] =
                        it["quantity"].toString().toInt() + element["quantity"].toString()
                            .toInt()
                    it
                } ?: element
            }
        val max = grouping?.maxByOrNull {
            it.value["quantity"].toString().toInt()
        }
        val id = max?.key
        if (id != null) {
            when (val result = drinkRepo.getDrink(id.toString())) {
                is Resource.OnSuccess -> {
                    _bestSelling.postValue(result.data!!.name.toString())
                }
                is Resource.OnFailure -> {
                    _bestSelling.postValue("")
                    container.showMessage(result.message.toString())
                }
            }
        } else {
            _bestSelling.postValue("")
        }
    }
}