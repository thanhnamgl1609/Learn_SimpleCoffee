package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.domain.usecase.user.GetCurrentUserInfoUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import java.time.LocalDate
import javax.inject.Inject

class GetCustomerOrderHistoryUseCase @Inject constructor(
    private val orderRepo: IOrderRepo,
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase
) {
    suspend operator fun invoke(): Resource<List<Order>?> {
        return getCurrentUserInfoUseCase().data?.run {
            orderRepo.getOrderHistory(id!!)
        } ?: Resource.OnFailure(null, ErrorConst.ERROR_AUTH)
    }

    suspend operator fun invoke(
        fromDate: LocalDate?,
        toDate: LocalDate?
    ): Resource<List<Order>?> {
        if (!isValid(fromDate, toDate))
            return Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)
        return getCurrentUserInfoUseCase().data?.run {
            orderRepo.getOrderHistory(id!!, fromDate!!, toDate!!)
        } ?: Resource.OnFailure(null, ErrorConst.ERROR_AUTH)
    }

    private fun isValid(fromDate: LocalDate?, toDate: LocalDate?) =
        (fromDate != null) && (toDate != null)
}