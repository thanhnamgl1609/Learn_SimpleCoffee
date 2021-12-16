package com.project.simplecoffee.domain.model

import com.google.firebase.Timestamp

data class UserInfo(
    var firstname: String? = null,
    var lastname: String? = null,
    val role: String? = null,
    val dob: Timestamp? = null,
    val avatar: String? = AVATAR_DEFAULT,
    var male: Boolean? = null,
) : Model() {
    companion object CONSTANT {
        const val AVATAR_DEFAULT =
            "https://firebasestorage.googleapis.com/v0/b/simple-64565.appspot.com/o/User%2Fdefault.jpg?alt=media&token=b9cf035d-a568-45e8-b0f4-fa92240ca1f6"

    }
}
