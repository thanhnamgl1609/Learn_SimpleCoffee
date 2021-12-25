package com.project.simplecoffee.presentation.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.usecase.order.GetCurrentOrderManagerUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import javax.inject.Inject

class OrderProcessingVM @Inject constructor(
    private val container: MainContainer,
    private val getCurrentOrderManagerUseCase: GetCurrentOrderManagerUseCase
) : OrderVM(container) {

    init {
        getListOrder()
    }

    override suspend fun getOrder() = getCurrentOrderManagerUseCase()

    override fun onItemClick(order: Order) {
        container.loadFragment(
            AllMainFragment.OrderDetailStaff.createFragment(order.id!!), true
        )
    }
}