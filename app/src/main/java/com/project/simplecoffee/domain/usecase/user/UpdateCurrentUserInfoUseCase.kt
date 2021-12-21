package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class UpdateCurrentUserInfoUseCase @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val userRepo: IUserRepo
) {
    suspend operator fun invoke(
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ) = getCurrentUserUseCase()?.run {
        userRepo.updateUserInfo(
            this.id!!, firstName, lastName, gender
        )
    } ?: Resource.OnFailure(null, ErrorConst.ERROR_AUTH)
}