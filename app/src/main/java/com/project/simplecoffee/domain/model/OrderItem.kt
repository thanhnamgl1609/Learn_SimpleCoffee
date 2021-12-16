package com.project.simplecoffee.domain.model

data class OrderItem(
    val drink: Drink,
    var quantity: Int,
    val price: Double?
)