package com.project.simplecoffee.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.R
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.common.getDayOfWeek
import com.project.simplecoffee.common.toCustomString
import com.project.simplecoffee.constant.CustomConstant
import com.project.simplecoffee.domain.models.Order
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.views.main.MainContainer
import kotlinx.coroutines.launch

class OrderItemVM constructor(
    val container: MainContainer,
    val revenueVM: RevenueVM,
    val drinkRepo: IDrinkRepo,
    val drinkCategoryRepo: IDrinkCategoryRepo,
    val order: Order
) : ViewModel(), ItemVM {
    override val viewType = R.layout.order_item

    private val _dayOfWeek = MutableLiveData(order.createdAt!!.getDayOfWeek())
    private val _monthYear =
        MutableLiveData(order.createdAt!!.toCustomString(CustomConstant.FULL_MONTH_YEAR))
    private val _dayOfMonth =
        MutableLiveData(order.createdAt!!.toCustomString(CustomConstant.DAY_OF_MONTH))
    private val _status = MutableLiveData(order.status.toString())
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
        order.drink?.let {
            var listDrink = it
            if (listDrink.size > 3) {
                listDrink = it.take(2)
                _moreVisible.postValue(View.VISIBLE)
            } else
                _moreVisible.postValue(View.GONE)
            val listItemVM = mutableListOf<DrinkOrderItemVM>()
            for (drinkInfo in listDrink.take(3)) {
                val drinkID = drinkInfo["drink"].toString()
                val quantity = drinkInfo["quantity"].toString()
                val unitPrice = drinkInfo["unit"].toString()
                when (val result = drinkRepo.getDrink(drinkID)) {
                    is Resource.OnSuccess -> {
                        val drink = result.data!!
                        val drinkCategory = drink.category?.run {
                            drinkCategoryRepo.getDrinkCategory(first()).data?.name
                        }
                        listItemVM.add(
                            DrinkOrderItemVM(
                                drink,
                                quantity,
                                unitPrice,
                                drinkCategory ?: ""
                            )
                        )
                    }
                    is Resource.OnFailure -> {
                        container.showMessage(result.message.toString())
                    }
                }
            }
            _liveListItemVM.postValue(listItemVM)
        }
    }
}