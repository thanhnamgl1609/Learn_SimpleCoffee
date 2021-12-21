package com.project.simplecoffee.domain.model

data class Cart(
    var table: String? = null,
    var mail: String? = null,
    var contact: Contact? = null,
    var items: MutableList<OrderItem>? = null
) : Model()