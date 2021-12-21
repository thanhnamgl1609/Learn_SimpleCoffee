package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.usecase.inventory.CheckAvailableDrinkUseCase
import com.project.simplecoffee.domain.usecase.user.GetCartUserUseCase
import com.project.simplecoffee.domain.usecase.user.SaveCartUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddDrinkToCartUseCase @Inject constructor(
    private val getCartUserUseCase: GetCartUserUseCase,
    private val checkAvailableDrinkUseCase: CheckAvailableDrinkUseCase,
    private val saveCartUseCase: SaveCartUseCase
) {
    suspend operator fun invoke(drink: Drink): Resource<Cart?> =
        withContext(Dispatchers.IO) {
            getCartUserUseCase().data?.run {
                var available: Boolean? = null
                items?.run {
                    find { orderItem -> orderItem.drink.id == drink.id }?.apply {
                        available = checkAvailableDrinkUseCase(drink.id, quantity + 1).data
                        quantity += 1
                    } ?: run {
                        available = checkAvailableDrinkUseCase(drink.id, 1).data
                        add(OrderItem(drink, 1, drink.price))
                    }
                } ?: run {
                    available = checkAvailableDrinkUseCase(drink.id, 1).data
                    items = mutableListOf(OrderItem(drink, 1, drink.price))
                }
                if (available == true)
                    saveCartUseCase(this)
                else
                    Resource.OnFailure(null, ErrorConst.ERROR_OUT_OF_STOCK)
            } ?: Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
}