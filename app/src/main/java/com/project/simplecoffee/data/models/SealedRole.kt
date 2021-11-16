package com.project.simplecoffee.data.models

sealed class SealedRole(val role: String) {
    object MANAGER : SealedRole("manager")
    object STAFF : SealedRole("staff")
    object CUSTOMER : SealedRole("customer")
}