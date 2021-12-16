package com.project.simplecoffee.domain.model

import androidx.annotation.NonNull
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
open class Model {
    @Exclude @JvmField
    var id: String ?= null;

    fun <T : Model> withId(@NonNull id: String): T {
        this.id = id;
        return this as T;
    }
}