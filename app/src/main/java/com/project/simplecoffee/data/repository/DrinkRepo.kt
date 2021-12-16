package com.project.simplecoffee.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.mapper.DrinkMapper
import com.project.simplecoffee.data.model.DrinkDB
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.repository.IDrinkRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.NullPointerException
import javax.inject.Inject

class DrinkRepo @Inject constructor(
    private val drinkMapper: DrinkMapper
) : IDrinkRepo {
    private val collection = FirebaseFirestore.getInstance().collection("drink")

    @DelicateCoroutinesApi
    override suspend fun getAllDrink()
            : Resource<List<Drink>?> = withContext(Dispatchers.IO) {
        try {
            val listDrink = mutableListOf<Drink>()
            val listDocuments = collection.get().await()
            for (document in listDocuments) {
                val drinkDB = document.toObject(DrinkDB::class.java).withId<DrinkDB>(document.id)
                drinkMapper.toModel(drinkDB)?.run {
                    listDrink.add(this)
                }
            }
            Resource.OnSuccess(listDrink)
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    @DelicateCoroutinesApi
    override suspend fun getDrinkDetail(
        id: String
    ): Resource<Drink?> = withContext(Dispatchers.IO) {
        try {
            val document = collection.document(id).get().await()
            val drinkDB = document.toObject(DrinkDB::class.java)?.withId<DrinkDB>(document.id)
            val drink = drinkMapper.toModel(drinkDB)
            drink?.run { Resource.OnSuccess(drink) } ?: Resource.OnFailure(
                null,
                ErrorConst.ERROR_MAPPING
            )

        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    @DelicateCoroutinesApi
    override suspend fun getDrinkByCategory(drinkCategoryID: String): Resource<List<Drink>?> =
        withContext(Dispatchers.IO) {
            try {
                val documents = collection.whereArrayContains("category", drinkCategoryID)
                    .get().await()
                val listDrink = mutableListOf<Drink>()
                documents.forEach { document ->
                    val drinkDB =
                        document.toObject(DrinkDB::class.java).withId<DrinkDB>(document.id)
                    drinkMapper.toModel(drinkDB)?.run {
                        listDrink.add(this)
                    }
                }
                Resource.OnSuccess(listDrink)

            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }
}