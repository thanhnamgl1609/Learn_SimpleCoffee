package com.project.simplecoffee.data.mapper

import android.util.Log
import com.project.simplecoffee.data.model.OrderDB
import com.project.simplecoffee.domain.mapper.IModelMapper
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.utils.common.toLocalDateTime
import com.project.simplecoffee.utils.common.toTimestamp
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject

class OrderMapper @Inject constructor(
    private val orderItemMapper: OrderItemMapper
) : IModelMapper<Order, OrderDB> {
    override suspend fun fromModel(from: Order?): OrderDB? {
        return from?.run {
            val listDrink = mutableListOf<MutableMap<String, Any?>>()
            drink?.forEach {
                listDrink.add(orderItemMapper.fromModel(it)!!)
            }
            OrderDB(
                createdAt?.toTimestamp(),
                email,
                address,
                phone,
                status?.status,
                listDrink,
                total,
                table,
                staffID
            ).withId(id!!)
        }
    }

    @DelicateCoroutinesApi
    override suspend fun toModel(from: OrderDB?): Order? {
        return from?.run {
            val listDrink = mutableListOf<OrderItem>()
            drink?.forEach {
                orderItemMapper.toModel(it)?.run { listDrink.add(this) }
            }
            Order(
                createdAt?.toLocalDateTime(),
                email,
                address,
                phone,
                getStatus(status),
                listDrink,
                total,
                table,
                staffID
            ).withId(id!!)
        }
    }

    private fun getStatus(status: String?): OrderStatus? =
        when (status) {
            OrderStatus.Processing.status -> OrderStatus.Processing
            OrderStatus.Cancelled.status -> OrderStatus.Cancelled
            OrderStatus.RequestCancel.status -> OrderStatus.RequestCancel
            OrderStatus.Shipping.status -> OrderStatus.Shipping
            OrderStatus.Queueing.status -> OrderStatus.Queueing
            OrderStatus.Success.status -> OrderStatus.Success
            OrderStatus.WaitInStore.status -> OrderStatus.WaitInStore
            else -> null
        }
}