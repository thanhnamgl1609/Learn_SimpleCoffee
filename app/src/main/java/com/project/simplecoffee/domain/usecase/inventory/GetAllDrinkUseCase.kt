package com.project.simplecoffee.domain.usecase.inventory

import com.project.simplecoffee.domain.repository.IDrinkRepo
import javax.inject.Inject

class GetAllDrinkUseCase @Inject constructor(
    private val drinkRepo: IDrinkRepo
) {
    suspend operator fun invoke() = drinkRepo.getAllDrink()
}