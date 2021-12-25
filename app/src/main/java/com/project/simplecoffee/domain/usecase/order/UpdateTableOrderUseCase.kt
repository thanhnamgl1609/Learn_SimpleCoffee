package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.repository.ITableRepo
import javax.inject.Inject

class UpdateTableOrderUseCase @Inject constructor(
    private val tableRepo: ITableRepo
) {
    suspend operator fun invoke(id: String, orderID: String?) =
        tableRepo.updateTableOrder(id, orderID)
}