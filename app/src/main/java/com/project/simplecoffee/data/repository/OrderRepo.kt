package com.project.simplecoffee.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.mapper.OrderMapper
import com.project.simplecoffee.data.model.OrderDB
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.utils.common.toTimestamp
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDate
import javax.inject.Inject

class OrderRepo @Inject constructor(
    private val orderMapper: OrderMapper
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