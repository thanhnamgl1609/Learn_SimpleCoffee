package com.project.simplecoffee.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Order(
    @JvmField @PropertyName("created_at")
    val createdAt: Timestamp? = null,
    val uid: String? = null,
    val address: String? = null,
    val phone: String? = null,
    var status: String? = null,
    val drink: List<OrderItem>? = null,
    val total: Double? = null,
    val table: String? = null,
    @PropertyName("staff_id")
    val staffID: String? = null
) : Model()