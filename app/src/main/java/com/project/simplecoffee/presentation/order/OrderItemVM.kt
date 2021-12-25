package com.project.simplecoffee.presentation.order

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.R
import com.project.simplecoffee.utils.common.getDayOfWeek
import com.project.simplecoffee.utils.common.toCustomString
import com.project.simplecoffee.utils.constant.CustomConstant
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.presentation.inventory.DrinkOrderItemVM
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.presentation.common.ItemVM
import kotlinx.coroutines.launch

class OrderItemVM constructor(
    private val container: MainContainer,
    private val pViewModel: OrderVM,
    private val order: Order
) : ViewModel(), ItemVM {
    override val viewType = R.layout.order_item

    private val _dayOfWeek = MutableLiveData(order.createdAt!!.dayOfWeek.name)
    private val _monthYear =
        MutableLiveData(order.createdAt!!.toCustomString(CustomConstant.FULL_MONTH_YEAR))
    private val _dayOfMonth =
        MutableLiveData(order.createdAt!!.toCustomString(CustomConstant.DAY_OF_MONTH))
    private val _status = MutableLiveData(order.status?.status.toString())
    private val _total = MutableLiveData(order.total.toString())
    private val _liveListItemVM = MutableLiveData(emptyList<DrinkOrderItemVM>())
    private val _moreVisible = MutableLiveData(View.GONE)

    val dayOfWeek: LiveData<String>
        get() = _dayOfWeek
    val monthYear: LiveData<String>
        get() = _monthYear
    val dayOfMonth: LiveData<String>
        get() = _dayOfMonth
    val status: LiveData<String>
        get() = _status
    val total: LiveData<String>
        get() = _total
    val liveListItemVM: LiveData<List<DrinkOrderItemVM>>
        get() = _liveListItemVM
    val moreVisible: LiveData<Int>
        get() = _moreVisible

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        order.drink?.apply {
            val listOrderItem = if (size > 3) {
                _moreVisible.postValue(View.VISIBLE)
                take(2)
            } else {
                _moreVisible.postValue(View.GONE)
                take(3)
            }
            val listItemVM = mutableListOf<DrinkOrderItemVM>()
            for (orderItem in listOrderItem) {
                listItemVM.add(DrinkOrderItemVM(orderItem))
            }
            _liveListItemVM.postValue(listItemVM)
        }
    }

    fun onClick() {
        pViewModel.onItemClick(order)
    }
}