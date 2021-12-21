package com.project.simplecoffee.data.model

import com.google.firebase.firestore.*
import com.project.simplecoffee.domain.model.Model

data class CartDB(
    val table: String? = null,
    @JvmField @PropertyName("user_mail")
    val mail: String? = null,
    val contact: String? = null,
    var items: MutableList<MutableMap<String, Any?>>? = null
) : Model()