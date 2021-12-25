package com.project.simplecoffee.presentation.order

import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.repository.ITableRepo
import com.project.simplecoffee.utils.common.Resource
import javax.inject.Inject

class GetTableByIDUseCase @Inject constructor(
    private val tableRepo: ITableRepo
) {
    suspend operator fun invoke(id: String): Resource<Table?> = tableRepo.getTableByID(id)
}