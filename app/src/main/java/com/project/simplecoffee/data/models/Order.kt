package com.project.simplecoffee.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude

data class Order(
    val createdAt: Timestamp,
    val uid: String? = null,
    val address: String? = null,
    val phone: String? = null,
    var status: String,
    val total: Double,
    val staff_id: String? = null,
    val table: String? = null
) : Model() {
    @Exclude
    lateinit var orderStatus: OrderStatus
    lateinit var drink : MutableList<DocumentReference>

    fun setProperties(id: String) : Order {
        this.orderStatus = OrderStatus.valueOf(status.uppercase())
        // Get list of drink
        return this.withId(id);
    }
}

enum class OrderStatus(val status: String) {
    REQUEST_FOR_CANCELLING("Request for cancelling") {
        override fun nextState() : OrderStatus = SHIPPING
        fun acceptCancel(): OrderStatus = CANCEL
    },
    CANCEL("Cancel") {
        override fun nextState(): OrderStatus = this
    },
    PROCESSING("Processing") {
        override fun nextState(): OrderStatus = SHIPPING
    },
    SHIPPING("Shipping") {
        override fun nextState(): OrderStatus = SUCCESS
    },
    SUCCESS("Success") {
        override fun nextState(): OrderStatus = this
    };
    abstract fun nextState() : OrderStatus
}