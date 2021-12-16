package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.repository.IContactRepo
import javax.inject.Inject

class GetAllContactUseCase @Inject constructor(
    private val contactRepo: IContactRepo
) {
    suspend operator fun invoke() = contactRepo.getAllContact()
}