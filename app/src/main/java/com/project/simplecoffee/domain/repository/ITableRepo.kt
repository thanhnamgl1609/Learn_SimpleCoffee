package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.utils.common.Resource

interface ITableRepo {
    suspend fun getAllTable(): Resource<List<Table>?>
    suspend fun getAllAvailableTables(): Resource<List<Table>?>
    suspend fun updateTableOrder(id: String, orderID: String): Resource<Table?>

}