package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Order
import java.time.LocalDate

interface IOrderRepo {
    suspend fun getOrderHistory(
        uid: String
    ): Resource<List<Order>?>

    suspend fun getOrderHistory(
        uid: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Resource<List<Order>?>
}