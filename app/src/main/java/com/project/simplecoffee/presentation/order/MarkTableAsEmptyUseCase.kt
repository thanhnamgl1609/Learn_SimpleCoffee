package com.project.simplecoffee.presentation.order

import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.repository.ITableRepo
import com.project.simplecoffee.domain.usecase.order.GetOrderByIDUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class MarkTableAsEmptyUseCase @Inject constructor(
    private val getOrderByIDUseCase: GetOrderByIDUseCase,
    private val tableRepo: ITableRepo
) {
    suspend operator fun invoke(id: String?): Resource<Table?> =
        id?.run {
            getOrderByIDUseCase(id).data?.run {
                table?.run {
                    tableRepo.updateTableOrder(table, null)
                } ?: Resource.OnFailure(
                    null,
                    ErrorConst.UNABLE_TO_RETRIEVE_ORDER_DETAIL
                )
            } ?: Resource.OnFailure(null, ErrorConst.UNABLE_TO_RETRIEVE_ORDER_DETAIL)
        } ?: Resource.OnFailure(null, ErrorConst.UNABLE_TO_RETRIEVE_ORDER_DETAIL)
}