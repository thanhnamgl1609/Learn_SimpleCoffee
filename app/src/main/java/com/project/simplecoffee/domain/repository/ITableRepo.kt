package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.model.details.TableImage
import com.project.simplecoffee.domain.model.details.TableShape
import com.project.simplecoffee.utils.common.Resource

interface ITableRepo {
    suspend fun getAllTable(): Resource<List<Table>?>
    suspend fun getAllAvailableTables(): Resource<List<Table>?>
    suspend fun updateTableOrder(id: String, orderID: String?): Resource<Table?>
    suspend fun updateTableDetail(
        id: String,
        name: String,
        size: Int,
        url: TableImage?,
        shape: TableShape
    ): Resource<Table?>

    suspend fun deleteTable(id: String): Resource<Table?>
    suspend fun createTable(
        name: String,
        size: Int,
        no: Int,
        url: TableImage?,
        shape: TableShape
    ): Resource<Table?>

    suspend fun getTableByID(id: String): Resource<Table?>

}