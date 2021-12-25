package com.project.simplecoffee.presentation.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.presentation.common.ItemVM

class OrderItemDetailVM constructor(
    orderItem: OrderItem
) : ViewModel(), ItemVM {
    override val viewType = R.layout.drink_in_order_detail_item

    private val _name = MutableLiveData<String>(orderItem.drink.name)
    private val _bitmap = MutableLiveData<String>(orderItem.drink.image_url)
    private val _quantity = MutableLiveData(orderItem.quantity.toString())
    private val _unitPrice = MutableLiveData("\$" + orderItem.price)
    private val _category = MutableLiveData<String>().apply {
        value = orderItem.drink.category?.run {
            if (size > 0) {
                if (first().name == "All")
                    last().name
                else
                    first().name
            } else
                "None"
        }
    }

    val name: LiveData<String>
        get() = _name
    val bitmap: LiveData<String>
        get() = _bitmap
    val quantity: LiveData<String>
        get() = _quantity
    val unitPrice: LiveData<String>
        get() = _unitPrice
    val category: LiveData<String>
        get() = _category
}