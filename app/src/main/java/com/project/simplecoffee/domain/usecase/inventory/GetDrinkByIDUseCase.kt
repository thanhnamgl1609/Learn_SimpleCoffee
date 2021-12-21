package com.project.simplecoffee.domain.usecase.inventory

import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class GetDrinkByIDUseCase @Inject constructor(
    private val drinkRepo: IDrinkRepo
) {
    suspend operator fun invoke(id: String?): Resource<Drink?> =
        id?.run { drinkRepo.getDrinkDetail(id) }
            ?: Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
}