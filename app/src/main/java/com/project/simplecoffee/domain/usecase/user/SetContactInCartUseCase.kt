package com.project.simplecoffee.domain.usecase.user

import com.project.simplecoffee.domain.model.Cart
import com.project.simplecoffee.domain.model.Contact
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.ErrorConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetContactInCartUseCase @Inject constructor(
    private val getCartUserUseCase: GetCartUserUseCase,
    private val saveCartUseCase: SaveCartUseCase
) {
    suspend operator fun invoke(contact: Contact?): Resource<Cart?> =
        withContext(Dispatchers.IO) {
            getCartUserUseCase().data?.run {
                this.contact = contact
                saveCartUseCase(this)
            } ?: Resource.OnFailure(null, ErrorConst.ERROR_UNEXPECTED)
        }
}