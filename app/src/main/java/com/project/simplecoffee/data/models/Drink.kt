package com.project.simplecoffee.data.models

data class Drink(
    var name: String,
    val photoURL: String,
    val price: Double,
    val stock: Boolean
) : Model()
