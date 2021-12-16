package com.project.simplecoffee.domain.usecase.inventory

import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchDrinkUseCase @Inject constructor(
    private val getAllDrinkUseCase: GetAllDrinkUseCase
) {
    suspend operator fun invoke(query: String?): Resource<List<Drink>?> {
        val getAllDrink = getAllDrinkUseCase()
        return withContext(Dispatchers.IO) {
            val result = getAllDrink.data?.run {
                val listQueryDrink = if (query != null && query.isNotEmpty()) {
                    filter { item ->
                        item.name!!.contains(query, ignoreCase = true)
                    }
                } else
                    this
                Resource.OnSuccess(listQueryDrink)
            } ?: run {
                Resource.OnFailure(null, getAllDrink.message)
            }
            result as Resource<List<Drink>?>
        }
    }
}