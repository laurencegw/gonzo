package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.core.users.api.*
import com.binarymonks.gonzo.core.users.persistence.Spice
import com.binarymonks.gonzo.core.users.persistence.UserEntity
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class UserService : Users {
    lateinit var clock: Clock

    @Autowired
    lateinit var userRepo: UserRepo

    override fun createUser(user: UserNew): User {
        val password = user.password
        val pepper = BCrypt.gensalt(20)
        val encryptedPassword = BCrypt.hashpw(password, pepper)
        val userEntity = UserEntity(
                email = user.email,
                encryptedPassword = encryptedPassword,
                spice = Spice(pepper = pepper)
        )
        val createdUser = userRepo.save(userEntity)
        return createdUser.toUser()
    }

    override fun updateUser(user: UserUpdate): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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