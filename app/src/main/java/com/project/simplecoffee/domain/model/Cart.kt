package com.project.simplecoffee.domain.model

import com.google.firebase.firestore.*

data class Cart(
    val email: String,
    val table: DocumentReference? = null,
    @PropertyName("user_mail")
    val mail: String? = null,
    val total: Double? = null,
    val contact: Contact? = null,
    var items: MutableList<Map<String, Any>>? = null
) : Model()