package com.project.simplecoffee.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.domain.repository.IContactRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactRepo @Inject constructor(
    private val userRepo: IUserRepo
) : IContactRepo {
    private val collection = FirebaseFirestore.getInstance().collection("contact")

    override suspend fun getAllContact(): Resource<List<Contact>?> = withContext(Dispatchers.IO)
    {
        try {
            val listContact = mutableListOf<Contact>()
            val id = userRepo.getCurrentUser()?.id
            val documents = collection.whereEqualTo("uid", id).get().await()
            for (document in documents) {
                listContact.add(document.toObject(Contact::class.java).withId(document.id))
            }
            Resource.OnSuccess(listContact)
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun updateContact(
        id: String,
        name: String,
        address: String,
        phone: String
    ): Resource<Contact?> = withContext(Dispatchers.IO) {
        try {
            val contact = mapOf<String, Any>(
                "name" to name,
                "address" to address,
                "phone" to phone
            )
            collection.document(id).update(contact).await()
            Resource.OnSuccess(
                collection.document(id).get().await().toObject(Contact::class.java)
            )
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun createContact(
        name: String,
        address: String,
        phone: String
    ): Resource<Contact?> = withContext(Dispatchers.IO) {
        try {
            val uid = userRepo.getCurrentUser()?.id!!
            val newDocument = collection.document()
            val contact = Contact(
                uid,
                name,
                address,
                phone
            ).withId<Contact>(newDocument.id)
            newDocument.set(contact).await()
            Resource.OnSuccess(contact)
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun deleteContact(id: String): Resource<Contact?> =
        withContext(Dispatchers.IO) {
            try {
                collection.document(id).delete().await()
                Resource.OnSuccess(null)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }
}