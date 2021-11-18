package com.project.simplecoffee.repository

import com.project.simplecoffee.domain.repository.IOrderRepo

class OrderRepo constructor(
    val uid: String
) : IOrderRepo{
}