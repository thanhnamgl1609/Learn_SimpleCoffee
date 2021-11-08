package com.project.simplecoffee.data.models

import androidx.annotation.NonNull
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
open class Model {
    @Exclude
    lateinit var id: String;

    fun <T : Model> withId(@NonNull id: String): T {
        this.id = id;
        return this as T;
    }
}