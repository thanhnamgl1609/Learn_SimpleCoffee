package com.project.simplecoffee.domain.usecase.auth

import com.project.simplecoffee.domain.repository.IUserRepo
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepo: IUserRepo
) {
    suspend operator fun invoke() = userRepo.getCurrentUser()
}