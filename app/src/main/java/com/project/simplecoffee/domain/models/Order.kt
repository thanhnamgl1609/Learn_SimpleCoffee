package com.project.simplecoffee.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Order(
    @JvmField @PropertyName("created_at")
    val createdAt: Timestamp? = null,
    val uid: String? = null,
    val address: String? = null,
    val phone: String? = null,
    var status: String? = null,
    val drink: List<MutableMap<String, Any>>? = null,
    val total: Double? = null,
    val table: String? = null,
    @PropertyName("staff_id")
    val staffID: String? = null
) : Model()