package com.project.simplecoffee.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class UserInfo(
    val firstname: String? = null,
    val lastname: String? = null,
    val role: String? = null,
    val dob: Timestamp? = null,
    val contact: MutableList<DocumentReference>? = null
) : Model()
