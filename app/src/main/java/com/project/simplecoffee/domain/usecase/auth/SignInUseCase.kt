package com.project.simplecoffee.domain.usecase.auth

import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.utils.common.Resource
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepo: IUserRepo
) {
    suspend operator fun invoke(email: String, pwd: String): Resource<User?> = userRepo.signIn(email, pwd)
}