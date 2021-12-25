package com.project.simplecoffee.presentation.order

import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.model.details.TableImage
import com.project.simplecoffee.domain.model.details.TableShape
import com.project.simplecoffee.domain.repository.ITableRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class UpdateTableDetailUseCase @Inject constructor(
    private val tableRepo: ITableRepo
) {
    suspend operator fun invoke(id: String, name: String?, size: Int?, shape: TableShape?)
            : Resource<Table?> =
        getError(id, name, size, shape)?.run {
            Resource.OnFailure(null, toString())
        } ?: tableRepo.updateTableDetail(
            id,
            name!!,
            size!!,
            TableImage.getURL(size, shape!!),
            shape
        )

    private fun getError(id: String, name: String?, size: Int?, shape: TableShape?) = when {
        name.isNullOrEmpty() -> ErrorConst.ERROR_NOT_ENTER_NAME
        size == null -> ErrorConst.ERROR_NOT_ENTER_SIZE
        shape == null -> ErrorConst.ERROR_NOT_ENTER_SHAPE
        else -> null
    }
}