package com.project.simplecoffee.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import com.project.simplecoffee.domain.model.DrinkCategory
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.NullPointerException
import javax.inject.Inject

class DrinkCategoryRepo : IDrinkCategoryRepo {
    private val collection = FirebaseFirestore.getInstance()
        .collection("drink_category")

    override suspend fun getAllDrinkCategory(): Resource<List<DrinkCategory>?> =
        withContext(Dispatchers.IO) {
            try {
                val listDrinkCategory = mutableListOf<DrinkCategory>()
                val listDocuments = collection.get().await()
                for (document in listDocuments) {
                    listDrinkCategory.add(
                        document.toObject(DrinkCategory::class.java).withId(document.id)
                    )
                }
                Resource.OnSuccess(listDrinkCategory)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun getDrinkCategoryDetailByID(id: String): Resource<DrinkCategory?> =
        withContext(Dispatchers.IO) {
            try {
                val document = collection.document(id).get().await()
                val drinkCategory = document.toObject(DrinkCategory::class.java)
                    ?.withId<DrinkCategory>(document.id)
                Resource.OnSuccess(drinkCategory)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }
}