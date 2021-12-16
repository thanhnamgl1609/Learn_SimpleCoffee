package com.project.simplecoffee.domain.usecase.order

import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class GetDrinkDetailUseCase @Inject constructor(
    private val drinkRepo: IDrinkRepo
) {
    suspend operator fun invoke(id: String?) =
        id?.run {
            drinkRepo.getDrinkDetail(id)
        } ?: Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)

}