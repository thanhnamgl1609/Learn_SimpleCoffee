package com.project.simplecoffee.domain.repository

import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Contact

interface IContactRepo {
    suspend fun getAllContact(): Resource<List<Contact>?>
    suspend fun updateContact(
        id: String,
        name: String,
        address: String,
        phone: String
    ): Resource<Contact?>

    suspend fun createContact(
        name: String,
        address: String,
        phone: String
    ): Resource<Contact?>

    suspend fun deleteContact(id: String): Resource<Contact?>
}