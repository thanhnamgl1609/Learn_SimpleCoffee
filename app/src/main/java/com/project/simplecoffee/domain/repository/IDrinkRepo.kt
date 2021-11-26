package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.Drink
import kotlinx.coroutines.Deferred

interface IDrinkRepo {
    suspend fun getAllDrink(): Resource<List<Drink>?>
    suspend fun getDrink(id: String): Resource<Drink?>
    fun loadBitMap(drink: Drink)
}