package io.tricefal.core.login

import java.time.Instant

data class LoginModel(val username: String,
                      var loginDate: Instant?,
                      var ipAddress: String?,
                      var city: String?,
                      var region: String?,
                      var device: String?,
                      var success: Boolean?)

fun toModel(domain: LoginDomain): LoginModel {
    return LoginModel(
            username = domain.username,
            loginDate = domain.loginDate,
            ipAddress = domain.ipAddress,
            city = domain.city,
            region = domain.region,
            device = domain.device,
            success = domain.success
    )
}

fun fromModel(model: LoginModel): LoginDomain {
    return LoginDomain(
            username = model.username,
            loginDate = model.loginDate,
            ipAddress = model.ipAddress,
            city = model.city,
            region = model.region,
            device = model.device,
            success = model.success
    )
}