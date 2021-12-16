package com.project.simplecoffee.domain.model

import com.google.firebase.firestore.DocumentReference

data class Table (
    var name: String,
    val processing: String? = null,
    val order: DocumentReference? = null
) : Model()
