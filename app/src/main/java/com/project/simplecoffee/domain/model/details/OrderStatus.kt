package com.project.simplecoffee.domain.model.details


sealed class OrderStatus(val status: String) {
    abstract fun next(): OrderStatus

    object WaitInStore : OrderStatus("Waiting") {
        override fun next() = Success
    }

    object Queueing : OrderStatus("Queueing") {
        override fun next() = Processing
    }

    object Processing : OrderStatus("Processing") {
        override fun next() = Shipping
    }

    object Shipping : OrderStatus("Shipping") {
        override fun next() = Success
    }

    object RequestCancel : OrderStatus("Request for cancelling") {
        override fun next() = Processing
    }

    object Cancelled : OrderStatus("Cancelled") {
        override fun next() = Cancelled
    }

    object Success : OrderStatus("Success") {
        override fun next() = Success
    }
}