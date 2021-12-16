package com.project.simplecoffee.domain.model.details


sealed class OrderStatus(val status: String) {
    object RequestCancel : OrderStatus("Request for cancelling")
    object Cancelled : OrderStatus("Cancelled")
    object Processing : OrderStatus("Processing")
    object Shipping : OrderStatus("Shipping")
    object Success : OrderStatus("Success")
}