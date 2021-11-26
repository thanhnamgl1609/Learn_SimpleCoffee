package com.project.simplecoffee.domain.models.details

sealed class Role(val value: String) {
    object Manager : Role("Manager")
    object Staff : Role("Staff")
    object Customer : Role("Customer")
}