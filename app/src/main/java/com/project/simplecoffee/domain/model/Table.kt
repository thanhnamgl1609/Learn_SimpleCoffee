package com.project.simplecoffee.domain.model

import com.project.simplecoffee.domain.model.details.TableShape

data class Table(
    var name: String? = null,
    val order: Order? = null,
    val no: Int? = null,
    val image: String? = null,
    val size: Int? = null,
    val shape: TableShape? = null
) : Model() {
}
