package com.project.simplecoffee.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.project.simplecoffee.domain.model.Model

data class OrderDB(
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