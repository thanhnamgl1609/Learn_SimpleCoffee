package com.project.simplecoffee.data.mapper

import com.project.simplecoffee.data.model.CartDB
import com.project.simplecoffee.domain.mapper.IModelMapper
import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.repository.IContactRepo
import javax.inject.Inject

class CartMapper @Inject constructor(
    private val contactRepo: IContactRepo,
    private val orderItemMapper: OrderItemMapper
) : IModelMapper<Cart, CartDB> {
    override suspend fun fromModel(from: Cart?): CartDB? =
        from?.run {
            val listDrink = mutableListOf<MutableMap<String, Any?>>()
            items?.forEach {
                listDrink.add(orderItemMapper.fromModel(it)!!)
            }
            CartDB(
                table,
                mail,
                contact?.id,
                listDrink
            ).withId(id!!)
        }

    override suspend fun toModel(from: CartDB?): Cart? = from?.run {
        val listDrink = mutableListOf<OrderItem>()
        items?.forEach {
            orderItemMapper.toModel(it)?.run { listDrink.add(this) }
        }
        val contactObject = contact?.run {
            contactRepo.getContactByID(this).data
        }
        Cart(
            table,
            mail,
            contactObject,
            listDrink
        ).withId(id!!)
    }
}