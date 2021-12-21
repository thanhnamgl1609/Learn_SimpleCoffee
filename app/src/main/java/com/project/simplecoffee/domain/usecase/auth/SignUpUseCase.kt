package com.project.simplecoffee.domain.usecase.auth

import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.domain.usecase.user.DeleteCurrentUserUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import java.time.LocalDate
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepo: IUserRepo,
) {
    suspend operator fun invoke(
        email: String?,
        pwd: String?,
        confirmPwd: String?,
        firstName: String?,
        lastName: String?,
        gender: Boolean?,
        dob: LocalDate?
    ): Resource<User?> {
        if (!isValid(email, pwd, confirmPwd)) {
            return Resource.OnFailure(
                null,
                ErrorConst.NOT_ALL_FILLED
            )
        }

        if (!isMatch(pwd, confirmPwd)) {
            return Resource.OnFailure(
                null,
                ErrorConst.ERROR_DIFF_PWD
            )
        }

        return userRepo.signUp(email!!, pwd!!, firstName!!, lastName!!, gender!!, dob!!)
    }

    private fun isValid(email: String?, pwd: String?, confirmPwd: String?) =
        !email.isNullOrEmpty() && !pwd.isNullOrEmpty() && !confirmPwd.isNullOrEmpty()

    private fun isMatch(pwd: String?, confirmPwd: String?) = pwd.equals(confirmPwd)
}