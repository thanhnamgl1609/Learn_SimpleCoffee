package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.model.details.OrderStatus
import java.time.LocalDate
import java.time.LocalDateTime

interface IOrderRepo {
    suspend fun getOrderHistory(
        email: String
    ): Resource<List<Order>?>

    suspend fun getOrderHistory(
        email: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Resource<List<Order>?>

    suspend fun createOrder(
        createdAt: LocalDateTime?,
        email: String?,
        address: String?,
        phone: String?,
        status: String,
        items: MutableList<OrderItem>,
        total: Double,
        table: String?,
        staffID: String?
    ): Resource<Order?>

    suspend fun getCurrentOrder(email: String): Resource<List<Order>?>
    suspend fun updateStatus(orderID: String, status: OrderStatus): Resource<Order?>
}