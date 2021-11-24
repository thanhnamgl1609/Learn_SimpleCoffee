package com.project.simplecoffee.domain.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.firestore.Exclude
import java.net.URL

data class Drink(
    var name: String? = null,
    val image_url: String? = null,
    val price: Double? = null,
    val stock: Long? = null,
    val category: List<String>? = null,
    @Exclude
    var bitmap: Bitmap? = null
) : Model()