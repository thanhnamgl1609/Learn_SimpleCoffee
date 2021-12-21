package com.project.simplecoffee.data.mapper

import com.project.simplecoffee.data.model.DrinkDB
import com.project.simplecoffee.domain.mapper.IModelMapper
import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.model.DrinkCategory
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import kotlinx.coroutines.*
import javax.inject.Inject

class DrinkMapper @Inject constructor(
    private val drinkCategoryRepo: IDrinkCategoryRepo
) : IModelMapper<Drink, DrinkDB> {
    override suspend fun fromModel(from: Drink?): DrinkDB? = from?.run {
        val listCategory = category?.map { name!! }
        DrinkDB(
            name,
            image_url,
            price,
            stock,
            listCategory
        ).withId(id!!)
    }

    @DelicateCoroutinesApi
    override suspend fun toModel(from: DrinkDB?): Drink? =
        from?.run {
            val listCategory = mutableListOf<DrinkCategory>()
            category?.forEach {
                drinkCategoryRepo.getDrinkCategoryDetailByID(it)
                    .data?.run {
                        listCategory.add(this)
                    }
            }
            Drink(
                name,
                image_url,
                price,
                stock,
                listCategory
            ).withId(id!!)
        }
}