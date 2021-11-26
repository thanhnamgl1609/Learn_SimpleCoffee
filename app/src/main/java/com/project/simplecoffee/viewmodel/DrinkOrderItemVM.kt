package com.project.simplecoffee.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.models.Drink

class DrinkOrderItemVM constructor(
    val drink: Drink,
    mQuantity: String,
    mPrice: String,
    mCategory: String
) : ViewModel(), ItemVM {
    override val viewType = R.layout.drink_in_order_item

    private val _name = MutableLiveData<String>(drink.name)
    private val _quantity = MutableLiveData(mQuantity)
    private val _unitPrice = MutableLiveData(mPrice)
    private val _category = MutableLiveData(mCategory)

    val name: LiveData<String>
        get() = _name
    val quantity: LiveData<String>
        get() = _quantity
    val unitPrice: LiveData<String>
        get() = _unitPrice
    val category: LiveData<String>
        get() = _category
}