package com.project.simplecoffee.domain.models

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class DrinkCategory(
    var name: String? = null,
    var image_url: String? = null
) : Model()