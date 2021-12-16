package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.repository.IUserInfoRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class UpdateCurrentUserInfoUseCase @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val userInfoRepo: IUserInfoRepo
) {
    suspend operator fun invoke(
        firstName: String?,
        lastName: String?,
        gender: Boolean?
    ) = getCurrentUserInfoUseCase().data?.run {
        userInfoRepo.updateUserInfo(
            this.id!!, firstName, lastName, gender
        )
    } ?: Resource.OnFailure(null, ErrorConst.ERROR_AUTH)
}