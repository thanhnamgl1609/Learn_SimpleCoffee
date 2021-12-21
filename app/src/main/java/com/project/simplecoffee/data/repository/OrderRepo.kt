package com.project.simplecoffee.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.mapper.OrderItemMapper
import com.project.simplecoffee.data.mapper.OrderMapper
import com.project.simplecoffee.data.model.OrderDB
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.OrderItem
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.utils.common.toTimestamp
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Error
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class OrderRepo @Inject constructor(
    private val orderMapper: OrderMapper,
    private val orderItemMapper: OrderItemMapper
) : IOrderRepo {
    private val collection = FirebaseFirestore.getInstance().collection("order")

    @DelicateCoroutinesApi
    override suspend fun getOrderHistory(
        uid: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Resource<List<Order>?> =
        withContext(Dispatchers.IO) {
            try {
                val start = startDate.toTimestamp()
                val to = endDate.atTime(23, 59, 59).toTimestamp()
                val listOrder = mutableListOf<Order>()
                val documents =
                    collection.orderBy("created_at")
                        .startAt(start).endAt(to).get().await()
                for (document in documents) {
                    orderMapper.toModel(
                        document.toObject(OrderDB::class.java).withId(document.id)
                    )?.apply { listOrder.add(this) }
                }
                Resource.OnSuccess(listOrder.filter { it.uid == uid })
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun createOrder(
        createdAt: LocalDateTime?,
        uid: String?,
        address: String?,
        phone: String?,
        status: String,
        items: MutableList<OrderItem>,
        total: Double,
        table: String?,
        staffID: String?
    ): Resource<Order?> = withContext(Dispatchers.IO) {
        try {
            val orderItems = mutableListOf<MutableMap<String, Any?>>()
            items.forEach { item ->
                orderItemMapper.fromModel(item)?.apply {
                    orderItems.add(this)
                }
            }

            val newDoc = collection.document()
            val orderDB = OrderDB(
                createdAt?.toTimestamp(),
                uid,
                address,
                phone,
                status,
                orderItems,
                total,
                table,
                staffID
            ).withId<OrderDB>(newDoc.id)
            newDoc.set(orderDB).await()
            Resource.OnSuccess(orderMapper.toModel(orderDB))
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun getCurrentOrder(uid: String): Resource<List<Order>?> =
        withContext(Dispatchers.IO) {
            try {
                val documents = collection
                    .whereNotIn(
                        "status",
                        listOf(OrderStatus.Success.status, OrderStatus.Cancelled.status)
                    )
                    .get().await()
                val listOrder = mutableListOf<Order>()
                documents.forEach { document ->
                    orderMapper.toModel(
                        document.toObject(OrderDB::class.java).withId(document.id)
                    )?.apply { listOrder.add(this) }
                }
                Resource.OnSuccess(listOrder)
            } catch (e: Exception) {
//                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
                Resource.OnFailure(null, e.message.toString())
            }
        }

    override suspend fun getOrderHistory(uid: String): Resource<List<Order>?> =
        withContext(Dispatchers.IO) {
            try {
                val listOrder = mutableListOf<Order>()
                val documents = collection.get().await()
                for (document in documents) {
                    orderMapper.toModel(
                        document.toObject(OrderDB::class.java).withId(document.id)
                    )?.apply { listOrder.add(this) }
                }
                Resource.OnSuccess(listOrder.filter { it.uid == uid })
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }
}