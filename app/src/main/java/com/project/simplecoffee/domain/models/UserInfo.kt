package com.project.simplecoffee.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class UserInfo(
    val firstname: String? = null,
    val lastname: String? = null,
    val role: String? = null,
    val dob: Timestamp? = null,
    @PropertyName("male")
    val isMale: Boolean? = null,
    val contact: MutableList<DocumentReference>? = null
) : Model()
