package io.oneprofile.core.user

interface IUserService {
    fun updatePassword(username: String, newPassword: String) : Boolean
}