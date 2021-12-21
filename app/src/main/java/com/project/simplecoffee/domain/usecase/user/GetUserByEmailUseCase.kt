package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.utils.common.Resource
import javax.inject.Inject

class GetUserByEmailUseCase @Inject constructor(
    private val userRepo: IUserRepo
) {
    suspend operator fun invoke(email: String?): Resource<User?> =
        userRepo.getUserByEmail(email)
}