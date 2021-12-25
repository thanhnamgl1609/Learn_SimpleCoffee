package com.project.simplecoffee.data.mapper

import com.project.simplecoffee.data.model.TableDB
import com.project.simplecoffee.domain.repository.IRevenueRepo
import com.project.simplecoffee.domain.mapper.IModelMapper
import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.model.details.TableShape
import javax.inject.Inject

class TableMapper @Inject constructor(
    private val revenueRepo: IRevenueRepo
) : IModelMapper<TableDB, Table> {
    override suspend fun fromModel(from: TableDB?): Table? =
        from?.run {
            Table(
                name,
                revenueRepo.getOrderByID(order).data,
                no,
                image,
                size,
                TableShape.getTableShape(shape!!)
            ).withId(id!!)
        }

    override suspend fun toModel(from: Table?): TableDB? =
        from?.run {
            TableDB(
                name,
                order?.id,
                no,
                image,
                size,
                shape?.value
            ).withId(id!!)
        }
}