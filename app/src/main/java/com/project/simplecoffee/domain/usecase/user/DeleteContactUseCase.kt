package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.domain.repository.IContactRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class DeleteContactUseCase @Inject constructor(
    private val contactRepo: IContactRepo
) {
    suspend operator fun invoke(id: String?): Resource<Contact?> {
        if (id == null)
            return Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)
        return contactRepo.deleteContact(id)
    }
}