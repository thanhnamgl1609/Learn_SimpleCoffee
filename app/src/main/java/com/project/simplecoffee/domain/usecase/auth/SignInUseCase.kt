package com.project.simplecoffee.domain.usecase.auth

import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepo: IUserRepo
) {
    suspend operator fun invoke(email: String, pwd: String)
            : Resource<User?> =
        (if (pwd[0] == ' ')
            Resource.OnFailure(
                null, ErrorConst.PASSWORD_START_WITH_EMPTY
            )
        else
            userRepo.signIn(email, pwd))


}