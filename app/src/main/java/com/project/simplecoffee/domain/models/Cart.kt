package com.project.simplecoffee.domain.models

import com.google.firebase.firestore.*

data class Cart(
    val email: String,
    val table: DocumentReference? = null,
    @PropertyName("user_mail")
    val mail: String? = null,
    val total: Double? = null,
    val contact: Contact? = null
) : Model() {
    @Exclude
    lateinit var items: MutableList<Map<String, Any>>

    fun setProperties(id: String, document: DocumentSnapshot) : Cart {
        // Get list of item

        return this.withId(id)
    }
}