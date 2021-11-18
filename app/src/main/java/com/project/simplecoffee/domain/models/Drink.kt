package com.project.simplecoffee.domain.models

data class Drink(
    var name: String,
    val photoURL: String,
    val price: Double,
    val stock: Boolean
) : Model()
