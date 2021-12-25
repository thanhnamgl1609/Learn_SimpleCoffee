package com.project.simplecoffee.presentation.order

import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.usecase.order.GetCurrentOrderCustomerUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import javax.inject.Inject

class OrderTrackingVM @Inject constructor(
    private val container: MainContainer,
    private val getCurrentOrderCustomerUseCase: GetCurrentOrderCustomerUseCase
) : OrderVM(container) {
    init {
        getListOrder()
    }

    override fun onItemClick(order: Order) {
        container.loadFragment(
            AllMainFragment.OrderDetailCustomer.createFragment(order.id!!),
            true
        )
    }

    override suspend fun getOrder() = getCurrentOrderCustomerUseCase()
}