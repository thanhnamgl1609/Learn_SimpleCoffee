package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.domain.repository.ICartRepo
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class GetCartUserUseCase @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val cartRepo: ICartRepo
) {
    suspend operator fun invoke() : Resource<Cart?> =
        getCurrentUserUseCase()?.run {
            cartRepo.getCart(id!!)
        } ?: Resource.OnFailure(null, ErrorConst.ERROR_AUTH)
}