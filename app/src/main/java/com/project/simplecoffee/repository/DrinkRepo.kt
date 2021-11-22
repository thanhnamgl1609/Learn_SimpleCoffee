package com.project.simplecoffee.repository

import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.constant.ErrorConst
import com.project.simplecoffee.domain.models.Drink
import com.project.simplecoffee.domain.repository.IDrinkRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

class DrinkRepo @Inject constructor(
    db: FirebaseFirestore
) : IDrinkRepo {
    private val collection = db.collection("drink")
    private var listDrink: MutableList<Drink>? = null

    override suspend fun getDrink()
            : Resource<List<Drink>?> = withContext(Dispatchers.IO) {
        try {
            if (listDrink == null) {
                listDrink = mutableListOf()
                val listDocuments = collection.get().await()
                for (document in listDocuments) {
                    val drink = document.toObject(Drink::class.java)
                    loadBitMap(drink)
                    listDrink?.add(drink.withId(document.id))
                }
            }
            Resource.OnSuccess(listDrink)
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    private fun loadBitMap(drink: Drink): Drink {
        val inputStream = URL(drink.image_url).openStream()
        drink.bitmap = BitmapFactory.decodeStream(inputStream)
        return drink
    }
}