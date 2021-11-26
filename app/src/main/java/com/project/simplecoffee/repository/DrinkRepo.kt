package com.project.simplecoffee.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.common.toBitMap
import com.project.simplecoffee.constant.ErrorConst
import com.project.simplecoffee.domain.models.Drink
import com.project.simplecoffee.domain.repository.IDrinkRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.NullPointerException
import javax.inject.Inject

class DrinkRepo @Inject constructor(
    db: FirebaseFirestore
) : IDrinkRepo {
    private val collection = db.collection("drink")
    private var listDrink: MutableList<Drink>? = null

    override suspend fun getAllDrink()
            : Resource<List<Drink>?> = withContext(Dispatchers.IO) {
        try {
            if (listDrink == null) {
                listDrink = mutableListOf()
                val listDocuments = collection.get().await()
                for (document in listDocuments) {
                    val drink = document.toObject(Drink::class.java).withId<Drink>(document.id)
                    loadBitMap(drink)
                    listDrink!!.add(drink)
                }
            }
            Resource.OnSuccess(listDrink)
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun getDrink(
        id: String
    ): Resource<Drink?> = withContext(Dispatchers.IO) {
        try {
            val filter = listDrink?.filter { drink -> id == drink.id }
            if (filter != null && filter.isNotEmpty()) {
                Resource.OnSuccess(filter.first())
            } else {
                val document = collection.document(id).get().await()
                val drink = document.toObject(Drink::class.java)?.withId<Drink>(document.id)
                    ?: throw NullPointerException()
                loadBitMap(drink)
                Resource.OnSuccess(drink)
            }
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override fun loadBitMap(drink: Drink) {
        if (drink.bitmap == null) {
            drink.bitmap = drink.image_url?.toBitMap()
        }
    }
}