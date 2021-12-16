package com.project.simplecoffee.domain.mapper

interface IModelMapper<E, V> {
    suspend fun fromModel(from: E?): V?
    suspend fun toModel(from: V?): E?
}