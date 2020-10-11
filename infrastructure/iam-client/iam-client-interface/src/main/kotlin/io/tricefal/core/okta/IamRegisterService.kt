package io.tricefal.core.okta

import io.tricefal.core.right.AccessRight
import io.tricefal.core.signup.SignupDomain

interface IamRegisterService {
    fun register(signup: SignupDomain): Boolean
    fun delete(username: String): Boolean
    fun addRole(username: String, role: AccessRight): Boolean
}