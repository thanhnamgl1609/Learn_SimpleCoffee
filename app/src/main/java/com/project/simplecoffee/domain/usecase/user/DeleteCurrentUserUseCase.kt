package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.repository.IUserRepo
import javax.inject.Inject

class DeleteCurrentUserUseCase @Inject constructor(
    private val userRepo: IUserRepo
) {
    suspend operator fun invoke() = userRepo.deleteCurrentUser()
}