package com.project.simplecoffee.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class UserInfo(
    var firstname: String,
    var lastname: String,
    val role: String,
    var dob: Timestamp
) : Model() {
    @Exclude
    lateinit var contacts: MutableList<DocumentReference>

    fun setProperties(id: String, document: DocumentSnapshot) : UserInfo{
        // Get list of contacts
        return this.withId(id)
    }

}
