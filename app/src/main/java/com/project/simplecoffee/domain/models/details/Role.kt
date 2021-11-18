package com.project.simplecoffee.domain.models.details

sealed class Role(val value: String) {
    object Manager : Role("manager")
    object Staff : Role("staff")
    object Customer : Role("customer")
}