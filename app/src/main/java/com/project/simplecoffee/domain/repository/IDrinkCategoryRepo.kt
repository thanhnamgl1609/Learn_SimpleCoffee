package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.DrinkCategory

interface IDrinkCategoryRepo {
    suspend fun getDrinkCategory(): Resource<List<DrinkCategory>>
}