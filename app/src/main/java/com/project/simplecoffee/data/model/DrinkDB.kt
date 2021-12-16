package com.project.simplecoffee.data.model
import com.project.simplecoffee.domain.model.Model

data class DrinkDB(
    var name: String? = null,
    val image_url: String? = null,
    val price: Double? = null,
    val stock: Long? = null,
    val category: List<String>? = null
) : Model()