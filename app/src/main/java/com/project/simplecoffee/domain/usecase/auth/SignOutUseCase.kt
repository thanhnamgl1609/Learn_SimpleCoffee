package com.project.simplecoffee.domain.usecase.auth

import com.project.simplecoffee.domain.repository.IUserRepo
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val userRepo: IUserRepo
) {
    operator fun invoke() = userRepo.signOut()
}