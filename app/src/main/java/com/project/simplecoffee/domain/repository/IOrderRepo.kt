package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.OrderItem
import java.time.LocalDate
import java.time.LocalDateTime

interface IOrderRepo {
    suspend fun getOrderHistory(
        uid: String
    ): Resource<List<Order>?>

    suspend fun getOrderHistory(
        uid: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Resource<List<Order>?>

    suspend fun createOrder(
        createdAt: LocalDateTime?,
        uid: String?,
        address: String?,
        phone: String?,
        status: String,
        items: MutableList<OrderItem>,
        total: Double,
        table: String?,
        staffID: String?
    ): Resource<Order?>

    suspend fun getCurrentOrder(uid: String): Resource<List<Order>?>
}