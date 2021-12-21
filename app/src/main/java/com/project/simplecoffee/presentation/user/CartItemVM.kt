package com.project.simplecoffee.presentation.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.presentation.common.ItemVM
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.presentation.inventory.MenuVM

class CartItemVM constructor(
    private val cartVM: CartVM,
    private val container: MainContainer,
    val orderItem: OrderItem
) : ViewModel(), ItemVM {
    override val viewType: Int = R.layout.cart_row_item

    val name = MutableLiveData<String>(orderItem.drink.name)
    val price = MutableLiveData(orderItem.drink.price.toString())
    val bitmap = MutableLiveData<String>(orderItem.drink.image_url)
    val quantity = MutableLiveData(orderItem.quantity.toString())

    fun onIncreaseClick() {
        if (cartVM.increase(orderItem)) {
            quantity.value = orderItem.quantity.toString()
        }
    }

    fun onDecreaseClick() {
        cartVM.decrease(orderItem)
        quantity.value = orderItem.quantity.toString()
    }
}