package com.project.simplecoffee.domain.model.details

sealed class TableShape(val value: String) {
    object Rectangle : TableShape("Rectangle")
    object Circle : TableShape("Circle")
    companion object {
        fun getTableShape(shape: String): TableShape? =
            when (shape) {
                Rectangle.value -> Rectangle
                Circle.value -> Circle
                else -> null
            }
    }
}