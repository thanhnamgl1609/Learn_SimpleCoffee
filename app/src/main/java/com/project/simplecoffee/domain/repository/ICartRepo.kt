package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.utils.common.Resource

interface ICartRepo {
    suspend fun getCart(id: String): Resource<Cart?>
    suspend fun updateCart(cart: Cart): Resource<Cart?>
}