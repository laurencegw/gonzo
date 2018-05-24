package com.binarymonks.gonzo.core.users.service

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

interface Passwords {
    fun genSalt(logRounds: Int): String
    fun hashPassword(password: String, salt: String): String
}

@Service
class BCryptPasswords : Passwords {
    override fun genSalt(logRounds: Int): String {
        return BCrypt.gensalt(logRounds)
    }

    override fun hashPassword(password: String, salt: String): String {
        return BCrypt.hashpw(password, salt)
    }
}