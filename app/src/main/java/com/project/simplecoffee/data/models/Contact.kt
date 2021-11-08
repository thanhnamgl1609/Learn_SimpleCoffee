package com.project.simplecoffee.data.models

data class Contact(
    val uid: String,
    var address: String,
    var phone: String
) : Model()