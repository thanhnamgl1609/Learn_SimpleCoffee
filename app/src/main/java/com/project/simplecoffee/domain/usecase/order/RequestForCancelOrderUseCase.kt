package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.presentation.order.IsAvailableForCancelOrderUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class RequestForCancelOrderUseCase @Inject constructor(
    private val isAvailableForCancelOrderUseCase: IsAvailableForCancelOrderUseCase,
    private val getOrderByIDUseCase: GetOrderByIDUseCase,
    private val orderRepo: IOrderRepo
) {
    suspend operator fun invoke(orderID: String?): Resource<Order?> =
        orderID?.run {
            if (!isAvailableForCancelOrderUseCase(orderID))
                Resource.OnFailure(
                    getOrderByIDUseCase(orderID).data,
                    ErrorConst.UNABLE_TO_CANCEL
                )
            else {
                orderRepo.updateStatus(orderID, OrderStatus.RequestCancel)
            }
        } ?: Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
}