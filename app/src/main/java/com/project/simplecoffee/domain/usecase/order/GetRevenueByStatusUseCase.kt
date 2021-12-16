package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.details.OrderStatus
import com.project.simplecoffee.domain.repository.IRevenueRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import java.time.LocalDate
import javax.inject.Inject

class GetRevenueByStatusUseCase @Inject constructor(
    private val getAllRevenueFromToUseCase: GetRevenueFromToUseCase
) {
    suspend operator fun invoke(
        startDate: LocalDate?,
        endDate: LocalDate?,
        status: OrderStatus
    ): Resource<List<Order>?> {
        if (!isValid(startDate, endDate))
            return Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)
        val resAllRevenue =
            getAllRevenueFromToUseCase(startDate, endDate)
        resAllRevenue.data?.apply {
            filter { order -> order.status == status.status }
            return Resource.OnSuccess(this)
        }
        return resAllRevenue
    }

    private fun isValid(startDate: LocalDate?, endDate: LocalDate?) =
        (startDate != null) && (endDate != null)
}