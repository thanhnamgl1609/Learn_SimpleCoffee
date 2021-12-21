package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.domain.repository.ICartRepo
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import javax.inject.Inject

class SaveCartUseCase @Inject constructor(
    private val cartRepo: ICartRepo
) {
    suspend operator fun invoke(cart: Cart?): Resource<Cart?> = cart?.run {
        cartRepo.updateCart(cart)
    } ?: Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
}