package com.project.simplecoffee.domain.model

import com.google.firebase.firestore.DocumentReference

data class Table(
    var name: String? = null,
    val order: Order? = null,
    val no: Int? = null,
    val image: String? = null,
    val size: Int? = null
) : Model()
