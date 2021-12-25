package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class NextStatusOrderProcessingUseCase @Inject constructor(
    private val getOrderByIDUseCase: GetOrderByIDUseCase,
    private val orderRepo: IOrderRepo
) {
    suspend operator fun invoke(id: String?): Resource<Order?> =
        id?.run {
            getOrderByIDUseCase(id).data?.status?.run {
                orderRepo.updateStatus(id, next())
            } ?: Resource.OnFailure(null, ErrorConst.UNABLE_TO_RETRIEVE_ORDER_DETAIL)
        } ?: Resource.OnFailure(null, ErrorConst.UNABLE_TO_RETRIEVE_ORDER_DETAIL)
}