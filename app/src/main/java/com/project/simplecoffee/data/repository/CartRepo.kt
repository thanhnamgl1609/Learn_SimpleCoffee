package com.project.simplecoffee.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.mapper.CartMapper
import com.project.simplecoffee.data.model.CartDB
import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.repository.ICartRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject

class CartRepo @Inject constructor(
    private val cartMapper: CartMapper
) : ICartRepo {
    private val collection = FirebaseFirestore.getInstance().collection("cart")

    override suspend fun getCart(id: String): Resource<Cart?> = withContext(Dispatchers.IO) {
        try {
            val document = collection.document(id).get().await()
            document.toObject(CartDB::class.java)?.withId<CartDB>(id)?.run{
                Resource.OnSuccess(cartMapper.toModel(this))
            } ?: run {
                val newCart = Cart(items = mutableListOf()).withId<Cart>(id)
                collection.document(id).set(newCart).await()
                Resource.OnSuccess(newCart)
            }
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun updateCart(cart: Cart): Resource<Cart?> =
        withContext(Dispatchers.IO) {
            try {
                cartMapper.fromModel(cart)?.run {
                    collection.document(id!!).set(this).await()
                    Resource.OnSuccess(cart)
                } ?: throw NullPointerException()
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }
}