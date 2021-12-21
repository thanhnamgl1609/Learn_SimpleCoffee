package com.project.simplecoffee.data.model

import com.project.simplecoffee.domain.model.Model

data class TableDB(
    var name: String? = null,
    val order: String? = null,
    val no: Int? = null,
    val image: String? = null,
    val size: Int? = null
) : Model()