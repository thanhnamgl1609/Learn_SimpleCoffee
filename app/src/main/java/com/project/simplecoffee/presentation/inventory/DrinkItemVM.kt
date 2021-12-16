package com.project.simplecoffee.presentation.inventory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.presentation.common.ItemVM

class DrinkItemVM(
    private val menuVM: MenuVM,
    val drink: Drink
) : ViewModel(), ItemVM {
    override val viewType: Int = R.layout.drink_row_item

    val name = MutableLiveData<String>(drink.name)
    val price = MutableLiveData(drink.price.toString())
    val bitmap = MutableLiveData<String>(drink.image_url)

    fun onAddToCartClick() {
        menuVM.addToCart(drink)
    }
}