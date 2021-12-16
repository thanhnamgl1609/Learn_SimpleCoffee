package com.project.simplecoffee.domain.model

data class Contact(
    val uid: String? = null,
    var name: String? = null,
    var address: String? = null,
    var phone: String? = null
) : Model()