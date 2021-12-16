package com.project.simplecoffee.domain.model

data class Drink(
    var name: String? = null,
    val image_url: String? = null,
    val price: Double? = null,
    val stock: Long? = null,
    val category: List<DrinkCategory>? = null
) : Model()