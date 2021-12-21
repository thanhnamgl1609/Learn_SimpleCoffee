package com.project.simplecoffee.data.model

import com.google.firebase.Timestamp
import com.project.simplecoffee.domain.model.Model

data class UserInfoDB (
    val email: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    val role: String? = null,
    val dob: Timestamp? = null,
    val avatar: String? = null,
    var male: Boolean? = null
) : Model()