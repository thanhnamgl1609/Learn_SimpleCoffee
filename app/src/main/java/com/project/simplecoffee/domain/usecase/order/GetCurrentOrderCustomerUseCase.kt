package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class GetCurrentOrderCustomerUseCase @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val orderRepo: IOrderRepo
) {
    suspend operator fun invoke(): Resource<List<Order>?> =
        getCurrentUserUseCase()?.run {
            orderRepo.getCurrentOrder(email!!)
        } ?: Resource.OnFailure(null, ErrorConst.ERROR_AUTH)
}