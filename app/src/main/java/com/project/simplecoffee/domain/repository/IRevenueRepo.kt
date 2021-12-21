package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.details.OrderStatus
import java.time.LocalDate

interface IRevenueRepo {
    suspend fun getRevenue(fromDate: LocalDate, toDate: LocalDate): Resource<List<Order>?>
    suspend fun getCurrentOrder(): Resource<List<Order>?>
    suspend fun getOrderByID(id: String?): Resource<Order?>
}