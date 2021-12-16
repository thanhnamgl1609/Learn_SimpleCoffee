package com.project.simplecoffee.domain.usecase.inventory

import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import javax.inject.Inject

class GetAllDrinkCategoryUseCase @Inject constructor(
    private val drinkCategoryRepo: IDrinkCategoryRepo
) {
    suspend operator fun invoke() = drinkCategoryRepo.getAllDrinkCategory()
}