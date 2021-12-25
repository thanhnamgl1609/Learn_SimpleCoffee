package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.presentation.order.IsAvailableForCancelOrderUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class AcceptForCancellingUseCase @Inject constructor(
    private val orderRepo: IOrderRepo
) {
    suspend operator fun invoke(orderID: String?): Resource<Order?> =
        orderID?.run {
            orderRepo.updateStatus(orderID, OrderStatus.Cancelled)
        } ?: Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
}