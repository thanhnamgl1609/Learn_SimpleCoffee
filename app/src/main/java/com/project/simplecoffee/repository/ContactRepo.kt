package com.project.simplecoffee.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.constant.ErrorConst
import com.project.simplecoffee.domain.models.Contact
import com.project.simplecoffee.domain.repository.IContactRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.NullPointerException

class ContactRepo constructor(
    private val uid: String
) : IContactRepo {
    private val collection = FirebaseFirestore.getInstance().collection("contact")
    private val listContact = mutableListOf<Contact>()
    override var current: Contact? = null

    override suspend fun getContact(): Resource<List<Contact>?> = withContext(Dispatchers.IO)
    {
        try {
            if (listContact.isEmpty()) {
                val documents = collection.whereEqualTo("uid", uid).get().await()
                for (document in documents) {
                    listContact.add(document.toObject(Contact::class.java).withId(document.id))
                }
            }
            Resource.OnSuccess(listContact)
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun updateContact(
        iname: String?,
        iaddress: String?,
        iphone: String?
    ): Resource<Contact?> = withContext(Dispatchers.IO) {
        try {
            if (!valid(iname, iaddress, iphone) || current == null)
                throw NullPointerException()
            current!!.apply {
                name = iname
                address = iaddress
                phone = iphone
                collection.document(this.id!!).set(this).await()
            }
            Resource.OnSuccess(current)
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun createContact(
        iname: String?,
        iaddress: String?,
        iphone: String?
    ): Resource<Contact?> = withContext(Dispatchers.IO) {
        try {
            if (!valid(iname, iaddress, iphone))
                throw NullPointerException()
            val newDocument = collection.document()
            val contact = Contact(
                uid,
                iname,
                iaddress,
                iphone
            ).withId<Contact>(newDocument.id)
            newDocument.set(contact).await()
            listContact.add(contact)
            Resource.OnSuccess(contact)
        } catch (e: Exception) {
            Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
    }

    override suspend fun deleteContact(): Resource<Contact?> =
        withContext(Dispatchers.IO) {
            try {
                if (current == null)
                    throw NullPointerException()
                listContact.remove(current!!)
                collection.document(current!!.id!!).delete().await()
                Resource.OnSuccess(null)
            } catch (e: Exception) {
                Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
            }
        }

    private fun valid(
        iname: String?,
        iaddress: String?,
        iphone: String?
    ): Boolean = (iname != null && iaddress != null && iphone != null)
}