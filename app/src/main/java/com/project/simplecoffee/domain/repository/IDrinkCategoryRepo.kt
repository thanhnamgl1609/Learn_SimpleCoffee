package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.DrinkCategory

interface IDrinkCategoryRepo {
    suspend fun getAllDrinkCategory(): Resource<List<DrinkCategory>?>
    suspend fun getDrinkCategoryDetailByID(id: String): Resource<DrinkCategory?>
}