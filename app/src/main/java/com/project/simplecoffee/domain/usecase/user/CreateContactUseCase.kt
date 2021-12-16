package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.data.repository.ContactRepo
import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class CreateContactUseCase @Inject constructor(
    private val contactRepo: ContactRepo
) {
    suspend operator fun invoke(
        name: String?,
        address: String?,
        phone: String?
    ): Resource<Contact?> {
        if (!valid(name, address, phone))
            return Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)
        return contactRepo.createContact(name!!, address!!, phone!!)
    }

    private fun valid(name: String?, address: String?, phone: String?) =
        !name.isNullOrEmpty()
                && !address.isNullOrEmpty() && !phone.isNullOrEmpty()
}