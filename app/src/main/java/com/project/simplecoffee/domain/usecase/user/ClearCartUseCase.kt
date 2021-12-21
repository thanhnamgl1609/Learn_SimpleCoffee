package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val getCartUserUseCase: GetCartUserUseCase,
    private val saveCartUseCase: SaveCartUseCase
) {
    suspend operator fun invoke(): Resource<Cart?> =
        getCartUserUseCase().data?.run {
            items?.clear()
            table = null
            mail = null
            saveCartUseCase(this)
        } ?: Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
}