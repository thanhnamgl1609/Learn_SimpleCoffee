package com.project.simplecoffee.domain.repository

import com.google.firebase.Timestamp
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.Order

interface IOrderRepo {
    var current: Order?

    suspend fun getOrderHistory(): Resource<List<Order>?>
    suspend fun getOrderFromTo(
        startDate: Timestamp,
        endDate: Timestamp
    ): Resource<List<Order>?>
}