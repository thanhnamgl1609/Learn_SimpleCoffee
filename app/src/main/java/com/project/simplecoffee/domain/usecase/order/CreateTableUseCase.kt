package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.model.details.TableImage
import com.project.simplecoffee.domain.model.details.TableShape
import com.project.simplecoffee.domain.repository.ITableRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class CreateTableUseCase @Inject constructor(
    private val tableRepo: ITableRepo,
    private val getAllTableUseCase: GetAllTableUseCase
) {
    suspend operator fun invoke(name: String?, size: Int?, shape: TableShape?)
            : Resource<Table?> =
        getError(name, size, shape)?.run {
            Resource.OnFailure(null, toString())
        } ?: run {
            getAllTableUseCase().data?.run {
                tableRepo.createTable(
                    name!!,
                    size!!,
                    this.size + 1,
                    TableImage.getURL(size, shape!!),
                    shape
                )
            } ?: Resource.OnFailure(null, ErrorConst.ERROR_ACCESS)
        }

    private fun getError(name: String?, size: Int?, shape: TableShape?) = when {
        name.isNullOrEmpty() -> ErrorConst.ERROR_NOT_ENTER_NAME
        size == null -> ErrorConst.ERROR_NOT_ENTER_SIZE
        shape == null -> ErrorConst.ERROR_NOT_ENTER_SHAPE
        else -> null
    }
}