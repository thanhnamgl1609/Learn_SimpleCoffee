package com.project.simplecoffee.domain.model.details

sealed class Gender(val value: String, val index: Int) {
    object Male : Gender("Male", 0)
    object Female : Gender("Female", 1)
}