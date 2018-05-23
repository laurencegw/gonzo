package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.core.users.api.*
import com.binarymonks.gonzo.core.users.persistence.Spice
import com.binarymonks.gonzo.core.users.persistence.UserEntity
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class UserService : Users {
    lateinit var clock: Clock

    @Autowired
    lateinit var userRepo: UserRepo
    @Autowired
    lateinit var passwords: Passwords

    @Value("\${bcrypt.logrounds}")
    var pwdLogRounds: Int = 10

    override fun createUser(user: UserNew): User {
        val password = user.password
        val pepper = passwords.genSalt(pwdLogRounds)
        val encryptedPassword = passwords.hashPassword(password, pepper)
        val userEntity = UserEntity(
                email = user.email,
                encryptedPassword = encryptedPassword,
                spice = Spice(pepper = pepper)
        )
        val createdUser = userRepo.save(userEntity)
        return createdUser.toUser()
    }

    override fun updateUser(user: UserUpdate): User {
        val userEntity = userRepo.findById(user.id).get()
        userEntity.apply {
            email = user.email
            firstName = user.firstName
            lastName = user.lastName
        }
        return userRepo.save(userEntity).toUser()
    }

    override fun getUserByEmail(email: String): User {
        return userRepo.findByEmail(email).toUser()
    }

    override fun updatePassword(passwordUpdate: UserPasswordUpdate) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestPasswordResetToken(email: String): Token {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetPassword(token: String, newPassword: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

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