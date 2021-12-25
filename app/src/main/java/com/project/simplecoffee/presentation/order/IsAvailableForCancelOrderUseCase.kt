package com.project.simplecoffee.presentation.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.usecase.order.GetOrderByIDUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class IsAvailableForCancelOrderUseCase @Inject constructor(
    private val getOrderByIDUseCase: GetOrderByIDUseCase
) {
    suspend operator fun invoke(orderID: String?): Boolean =
        orderID?.run {
            getOrderByIDUseCase(orderID).data?.run {
                status == OrderStatus.Queueing
            } ?: false
        } ?: false
}