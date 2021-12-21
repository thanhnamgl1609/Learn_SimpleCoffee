package com.project.simplecoffee.data.mapper

import android.util.Log
import com.project.simplecoffee.domain.mapper.IModelMapper
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.repository.IDrinkRepo
import kotlinx.coroutines.*
import javax.inject.Inject

class OrderItemMapper @Inject constructor(
    private val drinkRepo: IDrinkRepo
) : IModelMapper<OrderItem, MutableMap<String, Any?>> {
    override suspend fun fromModel(from: OrderItem?): MutableMap<String, Any?>? {
        return from?.run {
            mutableMapOf(
                "quantity" to this.quantity,
                "unit" to this.price,
                "drink" to this.drink.id!!
            )
        }
    }

    @DelicateCoroutinesApi
    override suspend fun toModel(from: MutableMap<String, Any?>?): OrderItem? {
        return from?.run {
            drinkRepo.getDrinkDetail(from["drink"].toString()).data?.run {
                OrderItem(
                    this,
                    from["quantity"].toString().toInt(),
                    from["unit"]?.toString()?.toDouble()
                )
            }
        }
    }
}