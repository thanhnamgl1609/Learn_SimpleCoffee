package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.Contact

interface IContactRepo {
    var current: Contact?
    suspend fun getContact(): Resource<List<Contact>?>
    suspend fun updateContact(
        iname: String?,
        iaddress: String?,
        iphone: String?
    ): Resource<Contact?>

    suspend fun createContact(
        iname: String?,
        iaddress: String?,
        iphone: String?
    ): Resource<Contact?>

    suspend fun deleteContact(): Resource<Contact?>
}