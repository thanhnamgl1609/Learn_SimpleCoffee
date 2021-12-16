package com.project.simplecoffee.domain.usecase.inventory

import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class GetDrinkByCategoryUseCase @Inject constructor(
    private val drinkRepo: IDrinkRepo
) {
    suspend operator fun invoke(drinkCategoryID: String?): Resource<List<Drink>?> =
        drinkCategoryID?.run { drinkRepo.getDrinkByCategory(this) }
            ?: Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
}