package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.model.details.Role
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.order.UpdateTableOrderUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class MakeOrderUseCase @Inject constructor(
    private val clearCartUseCase: ClearCartUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val updateTableOrderUseCase: UpdateTableOrderUseCase,
    private val orderRepo: IOrderRepo
) {
    suspend operator fun invoke(cart: Cart?): Resource<Order?> =
        withContext(Dispatchers.IO) {
            var result: Resource<Order?> = Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            val now = LocalDateTime.now()
            if (!isValidTime(now)) {
                result = Resource.OnFailure(null, ErrorConst.NOT_SERVICE_TIME)
            } else
                cart?.run {
                    if (items.isNullOrEmpty()) {
                        result = Resource.OnFailure(null, ErrorConst.ERROR_NOT_SELECT_DRINK)
                    } else {
                        val user = getCurrentUserUseCase()
                        var uid = user?.id
                        var staffID: String? = null
                        val isStaff = (user?.role is Role.Staff
                                || user?.role is Role.Manager)
                        if (isStaff) {
                            uid = getUserByEmailUseCase(cart.mail).data?.id
                            staffID = user?.id
                        }
                        val total = items!!.sumOf { orderItem ->
                            orderItem.quantity * orderItem.drink.price!!
                        }
                        result = orderRepo.createOrder(
                            now,
                            uid,
                            contact?.address,
                            contact?.phone,
                            OrderStatus.Queueing.status,
                            items!!,
                            total,
                            table,
                            staffID
                        )
                        if (result is Resource.OnSuccess) {
                            table?.apply { updateTableOrderUseCase(this, result.data!!.id!!) }
                            clearCartUseCase()
                        }
                    }
                }
            result
        }

    private fun isValidTime(now: LocalDateTime): Boolean {
        val today = now.toLocalDate()
        return now.isAfter(
            LocalDateTime.of(
                today,
                LocalTime.of(7, 30)
            )
        ) && now.isBefore(
            LocalDateTime.of(
                today,
                LocalTime.of(22, 0)
            )
        )
    }
}