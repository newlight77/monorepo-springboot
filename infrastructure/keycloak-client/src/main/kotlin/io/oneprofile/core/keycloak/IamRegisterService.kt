package io.oneprofile.core.keycloak

import io.oneprofile.core.right.AccessRight
import io.oneprofile.core.signup.SignupDomain

interface IamRegisterService {
    fun register(signup: SignupDomain): Boolean
    fun delete(username: String): Boolean
    fun updatePassword(username: String, newPassword: String): Boolean
    fun addRole(username: String, role: AccessRight): Boolean
}