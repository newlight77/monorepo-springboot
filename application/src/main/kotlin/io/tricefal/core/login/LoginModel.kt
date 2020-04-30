package io.tricefal.core.login

import java.time.Instant

data class LoginModel(var id: Long,
                        val username: String,
                        val lastLogin: Instant,
                        val ipAddress: String,
                        val success: Boolean)

fun toModel(domain: LoginDomain): LoginModel {
    return LoginModel(
            domain.id,
            domain.username,
            domain.lastLogin,
            domain.ipAddress,
            domain.success
    )
}

fun fromModel(model: LoginModel): LoginDomain {
    return LoginDomain(
            model.id,
            model.username,
            model.lastLogin,
            model.ipAddress,
            model.success
    )
}