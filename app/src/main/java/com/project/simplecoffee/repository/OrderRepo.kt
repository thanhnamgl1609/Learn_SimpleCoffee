package com.project.simplecoffee.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.Order
import com.project.simplecoffee.domain.repository.IOrderRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class OrderRepo constructor(
    val uid: String
) : IOrderRepo {
    private val collection = FirebaseFirestore.getInstance().collection("order")
    private val listOrderHistory = mutableListOf<Order>()

    override var current: Order? = null

    override suspend fun getOrderHistory(): Resource<List<Order>?> {
        TODO()
    }

    override suspend fun getOrderFromTo(
        startDate: Timestamp,
        endDate: Timestamp
    ): Resource<List<Order>?> =
        withContext(Dispatchers.IO) {
            try {
                val listOrder = mutableListOf<Order>()
                val documents =
                    collection.orderBy("created_at")
                        .startAt(startDate).endAt(endDate).get().await()
                for (document in documents) {
                    listOrder.add(
                        document.toObject(Order::class.java).withId(document.id)
                    )
                }
                Resource.OnSuccess(listOrder)
            } catch (e: Exception) {
                Resource.OnFailure(null, e.message.toString())
                // Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }
}