package com.project.simplecoffee.domain.model.details


sealed class OrderStatus(val status: String) {
    object Queueing : OrderStatus("Queueing")
    object Processing : OrderStatus("Processing")
    object Shipping : OrderStatus("Shipping")
    object RequestCancel : OrderStatus("Request for cancelling")
    object Cancelled : OrderStatus("Cancelled")
    object Success : OrderStatus("Success")
}