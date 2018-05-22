package com.binarymonks.gonzo.core.users.api

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime


interface Users {
    fun createUser(user: UserNew): User
    fun updateUser(user: UserUpdate): User
    fun getUserByEmail(email: String): User
    fun updatePassword(passwordUpdate: UserPasswordUpdate)
    fun requestPasswordResetToken(email:String): Token
    fun resetPassword(token: String, newPassword: String)
}

interface UserSignIn{
    fun login(credentials: LoginCredentials): Token
    fun signOutToken(token: String)
    fun signOutEmail(email: String)
    fun getUserByToken(token: String): User
}

data class UserNew @JsonCreator constructor(
        val email: String,
        val password: String
)

data class User @JsonCreator constructor(
        val id: Long,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null
)

data class UserUpdate @JsonCreator constructor(
        val id: Long,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null
)

data class LoginCredentials @JsonCreator constructor(
        val email: String,
        val password: String
)

data class Token @JsonCreator constructor(
        val token: String,
        val expires: ZonedDateTime
)

data class UserPasswordUpdate @JsonCreator constructor(
        val id: Long,
        val newPassword: String
)

