package io.oneprofile.signup.keycloak

import io.oneprofile.signup.right.AccessRight
import io.oneprofile.signup.signup.SignupDomain

interface IamRegisterService {
    fun register(signup: SignupDomain): Boolean
    fun delete(username: String): Boolean
    fun updatePassword(username: String, newPassword: String): Boolean
    fun addRole(username: String, role: AccessRight): Boolean
}