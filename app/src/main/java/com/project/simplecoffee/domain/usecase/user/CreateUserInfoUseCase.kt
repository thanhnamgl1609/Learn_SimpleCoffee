package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.UserInfo
import com.project.simplecoffee.domain.repository.IUserInfoRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import java.time.LocalDate
import javax.inject.Inject

class CreateUserInfoUseCase @Inject constructor(
    private val userInfoRepo: IUserInfoRepo
) {
    suspend operator fun invoke(
        id: String?,
        firstName: String?,
        lastName: String?,
        gender: Boolean?,
        dob: LocalDate?
    ): Resource<UserInfo?> {
        if (!isValid(id, firstName, lastName, gender, dob))
            return Resource.OnFailure(null, ErrorConst.NOT_ALL_FILLED)

        return userInfoRepo.createUserInfo(id!!, firstName!!, lastName!!, gender!!, dob!!)
    }

    private fun isValid(
        id: String?,
        firstName: String?,
        lastName: String?,
        gender: Boolean?,
        dob: LocalDate?
    ) = !id.isNullOrBlank() && !firstName.isNullOrBlank()
            && !lastName.isNullOrBlank() && gender != null && dob != null

}