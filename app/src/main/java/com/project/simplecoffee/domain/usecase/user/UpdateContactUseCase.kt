package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.domain.repository.IContactRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class UpdateContactUseCase @Inject constructor(
    private val contactRepo: IContactRepo
) {
    suspend operator fun invoke(
        id: String?,
        name: String?,
        address: String?,
        phone: String?
    ): Resource<Contact?> {
        if (!valid(id, name, address, phone))
            return Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)
        return contactRepo.updateContact(id!!, name!!, address!!, phone!!)
    }

    private fun valid(id: String?, name: String?, address: String?, phone: String?) =
        !id.isNullOrEmpty() &&
                !name.isNullOrEmpty() && !address.isNullOrEmpty() && !phone.isNullOrEmpty()
}