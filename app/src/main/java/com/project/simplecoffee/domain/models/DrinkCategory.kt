package com.project.simplecoffee.domain.models

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class DrinkCategory(
    var name: String,
    var photoURL: String,
) : Model() {
    @Exclude
    lateinit var drink: MutableList<DocumentReference>

    fun setProperties(id: String, document: DocumentSnapshot) : DrinkCategory {
        // Get list of drink
        return this.withId(id)
    }
}