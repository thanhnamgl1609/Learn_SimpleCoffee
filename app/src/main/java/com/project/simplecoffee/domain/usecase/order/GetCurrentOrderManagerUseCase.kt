package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.repository.IOrderRepo
import com.project.simplecoffee.domain.repository.IRevenueRepo
import com.project.simplecoffee.utils.common.Resource
import javax.inject.Inject

class GetCurrentOrderManagerUseCase @Inject constructor(
    private val revenueRepo: IRevenueRepo
) {
    suspend operator fun invoke() : Resource<List<Order>?> = revenueRepo.getCurrentOrder()
}