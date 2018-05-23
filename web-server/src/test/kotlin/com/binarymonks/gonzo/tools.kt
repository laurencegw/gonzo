package com.binarymonks.gonzo

import com.binarymonks.gonzo.core.users.service.Passwords

/**
 * Provides non random password hashing and salting for testing.
 */
class PasswordsStub() : Passwords {
    override fun genSalt(logRounds: Int): String {
        return "pepper"
    }

    override fun hashPassword(password: String, salt: String): String {
        return "$password hashed with $salt"
    }
}