package com.project.simplecoffee.data.repository

import com.project.simplecoffee.domain.repository.ICartRepo

class CartRepo constructor(
    val uid: String
) : ICartRepo {
}