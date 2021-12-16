package com.project.simplecoffee.domain.usecase.user

import android.util.Log
import com.project.simplecoffee.domain.model.UserInfo
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class GetCurrentUserInfoUseCase @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val userInfoRepo: IUserInfoRepo
) {
    suspend operator fun invoke(): Resource<UserInfo?> {
        val user = getCurrentUserUseCase()
            ?: return Resource.OnFailure(null, ErrorConst.ERROR_AUTH)
        return userInfoRepo.getUserInfo(user.id!!)
    }
}