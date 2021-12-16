package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Drink

interface IDrinkRepo {
    suspend fun getAllDrink(): Resource<List<Drink>?>
    suspend fun getDrinkDetail(id: String): Resource<Drink?>
    suspend fun getDrinkByCategory(drinkCategoryID: String): Resource<List<Drink>?>
}