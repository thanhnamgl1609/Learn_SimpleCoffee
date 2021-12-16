package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.repository.IRevenueRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import java.time.LocalDate
import javax.inject.Inject

class GetRevenueFromToUseCase @Inject constructor(
    private val revenueRepo: IRevenueRepo
) {
    suspend operator fun invoke(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Resource<List<Order>?> {
        if (!isValid(startDate, endDate))
            return Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)
        return revenueRepo.getRevenue(startDate!!, endDate!!)
    }

    private fun isValid(startDate: LocalDate?, endDate: LocalDate?) =
        (startDate != null) && (endDate != null)
}