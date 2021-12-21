package com.project.simplecoffee.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.project.simplecoffee.data.model.UserInfoDB
import com.project.simplecoffee.domain.mapper.IModelMapper
import com.project.simplecoffee.domain.model.User
import com.project.simplecoffee.domain.model.details.Gender
import com.project.simplecoffee.domain.model.details.Role
import com.project.simplecoffee.utils.common.toLocalDate
import com.project.simplecoffee.utils.common.toLocalDateTime
import com.project.simplecoffee.utils.common.toTimestamp

class UserMapper : IModelMapper<User, UserInfoDB> {
    override suspend fun fromModel(from: User?): UserInfoDB? =
        from?.run {
            UserInfoDB(
                email,
                firstname,
                lastname,
                role?.value,
                dob?.toTimestamp(),
                avatar,
                male is Gender.Male
            ).withId(id!!)
        }

    override suspend fun toModel(from: UserInfoDB?): User? =
        from?.run {
            User(
                email,
                null,
                firstname,
                lastname,
                getRole(role),
                dob?.toLocalDate(),
                avatar,
                getGender(male)
            ).withId(id!!)
        }

    fun getGender(male: Boolean?): Gender? =
        when (male) {
            true -> {
                Gender.Male
            }
            false -> Gender.Female
            else -> null
        }

    fun getRole(role: String?) =
        when (role) {
            Role.Manager.value -> Role.Manager
            Role.Staff.value -> Role.Staff
            Role.Customer.value -> Role.Customer
            else -> null
        }
}