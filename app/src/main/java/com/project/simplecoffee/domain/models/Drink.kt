package com.project.simplecoffee.domain.models

import android.graphics.Bitmap
import com.google.firebase.firestore.Exclude

data class Drink(
    var name: String? = null,
    val image_url: String? = null,
    val price: Double? = null,
    val stock: Long? = null,
    val category: List<String>? = null,
    @Exclude
    var bitmap: Bitmap? = null
) : Model()
