package com.project.simplecoffee.data.mapper

import android.util.Log
import com.project.simplecoffee.data.model.OrderDB
import com.project.simplecoffee.domain.mapper.IModelMapper
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.OrderItem
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
                createdAt,
                uid,
                address,
                phone,
                status,
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
                createdAt,
                uid,
                address,
                phone,
                status,
                listDrink,
                total,
                table,
                staffID
            ).withId(id!!)
        }
    }
}