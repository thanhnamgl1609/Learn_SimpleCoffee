package com.project.simplecoffee.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.mapper.OrderMapper
import com.project.simplecoffee.data.model.OrderDB
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.repository.IRevenueRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.common.toTimestamp
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDate
import javax.inject.Inject

class RevenueRepo @Inject constructor(
    private val orderMapper: OrderMapper
) : IRevenueRepo {
    private val collection = FirebaseFirestore.getInstance().collection("order")

    override suspend fun getRevenue(
        fromDate: LocalDate,
        toDate: LocalDate
    ): Resource<List<Order>?> =
        withContext(Dispatchers.IO) {
            try {
                val start = fromDate.toTimestamp()
                val to = toDate.atTime(23, 59, 59).toTimestamp()
                val listOrder = mutableListOf<Order>()
                val documents =
                    collection.orderBy("created_at")
                        .startAt(start).endAt(to).get().await()
                for (document in documents) {
                    val order = orderMapper.toModel(
                        document.toObject(OrderDB::class.java).withId(document.id)
                    )!!
                    listOrder.add(
                        order
                    )
                }
                Resource.OnSuccess(listOrder)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun getCurrentOrder(): Resource<List<Order>?> =
        withContext(Dispatchers.IO) {
            try {
                val listOrder = mutableListOf<Order>()
                val documents =
                    collection
                        .whereNotIn(
                            "status",
                            listOf(OrderStatus.Success.status, OrderStatus.Cancelled.status)
                        )
                        .get().await()
                for (document in documents) {
                    orderMapper.toModel(
                        document.toObject(OrderDB::class.java).withId(document.id)
                    )?.apply { listOrder.add(this) }
                }
                Resource.OnSuccess(listOrder)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun getOrderByID(id: String?): Resource<Order?> =
        withContext(Dispatchers.IO) {
            id?.run {
                try {
                    val document = collection.document(id).get().await()
                    val orderDB =
                        document.toObject(OrderDB::class.java)?.withId<OrderDB>(document.id)
                    Resource.OnSuccess(orderMapper.toModel(orderDB))
                } catch (e: Exception) {
                    Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
                }
            } ?: Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)
        }
}