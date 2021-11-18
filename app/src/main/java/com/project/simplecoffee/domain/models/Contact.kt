package com.project.simplecoffee.domain.models

data class Contact(
    val uid: String,
    var address: String,
    var phone: String
) : Model()