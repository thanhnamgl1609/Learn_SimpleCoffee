package com.project.simplecoffee.domain.model

import com.project.simplecoffee.domain.model.details.OrderStatus
import java.time.LocalDateTime

data class Order(
    val createdAt: LocalDateTime? = null,
    val email: String? = null,
    val address: String? = null,
    val phone: String? = null,
    var status: OrderStatus? = null,
    val drink: List<OrderItem>? = null,
    val total: Double? = null,
    val table: String? = null,
    val staffID: String? = null
) : Model()