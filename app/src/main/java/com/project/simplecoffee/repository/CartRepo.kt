package com.project.simplecoffee.repository

import com.project.simplecoffee.domain.repository.ICartRepo

class CartRepo constructor(
    val uid: String
) : ICartRepo {
}