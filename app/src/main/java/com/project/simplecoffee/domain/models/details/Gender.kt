package com.project.simplecoffee.domain.models.details

sealed class Gender(val value: String) {
    object Male : Role("Male")
    object FEMALE : Role("Female")
}