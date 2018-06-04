package com.binarymonks.gonzo.core.users.api

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime


interface Users {
    fun createUser(user: UserNew): User

    /**
     * Intended for users to update their own data.
     */
    fun updateUser(user: UserUpdate): User
    fun getUserByEmail(email: String): User
    fun requestPasswordResetToken(email: String): ResetToken
    fun resetPassword(passwordReset: PasswordReset)
    fun setUserRole(userID: Long, role: Role)
}

interface SignIn {
    fun login(credentials: LoginCredentials): String
    fun assertLoggedIn(token:String)
    fun getUserFromToken(token:String): User
}

data class UserNew @JsonCreator constructor(
        val email: String,
        val handle: String,
        val password: String
)

data class User @JsonCreator constructor(
        val id: Long,
        val email: String,
        val handle: String,
        val role: Role,
        val firstName: String? = null,
        val lastName: String? = null
) {
    fun toUpdate() = UserUpdate(
            id = id,
            email = email,
            firstName = firstName,
            lastName = lastName
    )

    fun toPublicHeader() = UserPublicHeader(
            id = id,
            handle = handle,
            firstName = firstName,
            lastName = lastName
    )
}

/**
 * Public information for a user
 */
data class UserPublicHeader @JsonCreator constructor(
        val id: Long,
        val handle: String,
        val firstName: String? = null,
        val lastName: String? = null
)

data class UserUpdate @JsonCreator constructor(
        val id: Long,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null
)

enum class Role{
    AUTHOR,
    ADMIN,
    READER
}

data class LoginCredentials @JsonCreator constructor(
        val email: String,
        val password: String
)

data class ResetToken @JsonCreator constructor(
        val expiry: ZonedDateTime,
        val token: String
)

data class PasswordReset @JsonCreator constructor(
        val userID: Long,
        val token: String,
        val newPassword: String
)

