package io.oneprofile.core.user

interface UserDataAdapter {
    fun updatePassword(username: String, newPassword: String): Boolean
}