package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.Drink

interface IDrinkRepo {
    suspend fun getDrink(): Resource<List<Drink>?>
}