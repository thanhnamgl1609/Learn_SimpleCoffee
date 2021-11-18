package com.project.simplecoffee.repository

import com.project.simplecoffee.domain.repository.IContactRepo
import com.project.simplecoffee.domain.repository.IUserInfoRepo

class ContactRepo constructor(
    val uid: String? = null,
    val userInfoRepo: IUserInfoRepo? = null
) : IContactRepo {
}