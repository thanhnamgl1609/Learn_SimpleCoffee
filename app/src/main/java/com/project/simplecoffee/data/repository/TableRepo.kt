package com.project.simplecoffee.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.data.mapper.TableMapper
import com.project.simplecoffee.data.model.TableDB
import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.repository.ITableRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class TableRepo @Inject constructor(
    private val tableMapper: TableMapper
) : ITableRepo {
    private val collection = FirebaseFirestore.getInstance().collection("table")

    override suspend fun getAllTable(): Resource<List<Table>?> =
        withContext(Dispatchers.IO) {
            try {
                val documents = collection.get().await()
                val tables = mutableListOf<Table>()
                documents.forEach { document ->
                    tableMapper.fromModel(
                        document.toObject(TableDB::class.java).withId(document.id)
                    )?.apply {
                        tables.add(this)
                    }
                }
                Resource.OnSuccess(tables)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun getAllAvailableTables(): Resource<List<Table>?> =
        withContext(Dispatchers.IO) {
            try {
                val documents = collection
                    .whereEqualTo("order", null)
                    .get().await()
                val tables = mutableListOf<Table>()
                documents.forEach { document ->
                    tableMapper.fromModel(
                        document.toObject(TableDB::class.java).withId(document.id)
                    )?.apply {
                        tables.add(this)
                    }
                }
                Resource.OnSuccess(tables)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    override suspend fun updateTableOrder(id: String, orderID: String): Resource<Table?> =
        withContext(Dispatchers.IO) {
            try {
                val updateInfo = mapOf("order" to orderID)
                collection.document(id).update(updateInfo).await()
                val document = collection.document(id).get().await()
                val tableDB = document.toObject(TableDB::class.java)?.withId<TableDB>(id)
                Resource.OnSuccess(tableMapper.fromModel(tableDB))
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }
}