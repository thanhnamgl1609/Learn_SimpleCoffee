package com.project.simplecoffee.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.domain.mapper.IModelMapper
import com.project.simplecoffee.domain.model.User

class UserMapper : IModelMapper<User, FirebaseUser> {
    override suspend fun fromModel(from: User?): FirebaseUser? {
        return null
    }

    override suspend fun toModel(from: FirebaseUser?): User? {
        if (from == null)
            return null
        return User(from.uid, from.email, null)
    }
}