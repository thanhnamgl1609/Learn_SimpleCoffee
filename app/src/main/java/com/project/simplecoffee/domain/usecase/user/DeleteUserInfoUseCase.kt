package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.UserInfo
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import com.project.simplecoffee.utils.common.Resource
import javax.inject.Inject

class DeleteUserInfoUseCase @Inject constructor(
    private val userInfoRepo: IUserInfoRepo
) {
    suspend operator fun invoke(id: String?) : Resource<UserInfo?>{
        if (id == null)
            return Resource.OnFailure(null, "User's ID is NULL")
        return userInfoRepo.deleteUserInfo(id)
    }
}