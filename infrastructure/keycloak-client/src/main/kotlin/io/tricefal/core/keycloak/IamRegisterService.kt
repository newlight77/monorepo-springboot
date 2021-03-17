package io.tricefal.core.keycloak

import io.tricefal.core.right.AccessRight
import io.tricefal.core.signup.SignupDomain

interface IamRegisterService {
    fun register(signup: SignupDomain): Boolean
    fun delete(username: String): Boolean
    fun updatePassword(username: String, password: String): Boolean
    fun addRole(username: String, role: AccessRight): Boolean
}