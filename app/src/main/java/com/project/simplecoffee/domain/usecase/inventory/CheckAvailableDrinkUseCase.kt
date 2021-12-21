package com.project.simplecoffee.domain.usecase.inventory

import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckAvailableDrinkUseCase @Inject constructor(
    private val getDrinkByIDUseCase: GetDrinkByIDUseCase
) {
    suspend operator fun invoke(id: String?, requestAmount: Int): Resource<Boolean?> =
        withContext(Dispatchers.IO) {
            getDrinkByIDUseCase(id).data?.run {
                Resource.OnSuccess(stock == null || stock >= requestAmount)
            } ?: Resource.OnFailure(null, ErrorConst.ERROR_UNABLE_TO_FIND_DRINK)
        }
}