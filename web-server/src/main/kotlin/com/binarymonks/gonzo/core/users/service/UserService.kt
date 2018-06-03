package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.core.common.ExpiredToken
import com.binarymonks.gonzo.core.common.InvalidCredentials
import com.binarymonks.gonzo.core.common.UniqueConstraintException
import com.binarymonks.gonzo.core.time.nowUTC
import com.binarymonks.gonzo.core.users.api.*
import com.binarymonks.gonzo.core.users.persistence.Spice
import com.binarymonks.gonzo.core.users.persistence.UserEntity
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Duration

@Service
class UserService : Users {
    var clock: Clock = java.time.Clock.systemUTC()

    @Autowired
    lateinit var userRepo: UserRepo
    @Autowired
    lateinit var passwords: Passwords

    var resetPasswordWindow: Duration = Duration.ofHours(1)

    @Value("\${bcrypt.logrounds}")
    var pwdLogRounds: Int = 10

    override fun createUser(user: UserNew): User {
        if (userRepo.findByNickName(user.nickname).isPresent) {
            throw UniqueConstraintException("nickname")
        }
        if (userRepo.findByEmail(user.email).isPresent) {
            throw UniqueConstraintException("email")
        }
        val password = user.password
        val pepper = passwords.genSalt(pwdLogRounds)
        val encryptedPassword = passwords.hashPassword(password, pepper)
        val userEntity = UserEntity(
                email = user.email,
                nickName = user.nickname,
                encryptedPassword = encryptedPassword,
                spice = Spice(pepper = pepper)
        )
        val createdUser = userRepo.save(userEntity)
        return createdUser.toUser()
    }

    override fun updateUser(user: UserUpdate): User {
        val userEntity = userRepo.findById(user.id).get()
        if (user.email != userEntity.email && userRepo.findByEmail(user.email).isPresent) {
            throw UniqueConstraintException("email")
        }
        userEntity.apply {
            email = user.email
            firstName = user.firstName
            lastName = user.lastName
        }
        return userRepo.save(userEntity).toUser()
    }

    override fun getUserByEmail(email: String): User {
        return userRepo.findByEmail(email).get().toUser()
    }

    override fun requestPasswordResetToken(email: String): ResetToken {
        val user = userRepo.findByEmail(email).get()
        val resetToken = generateToken(user.email)
        val expiry = nowUTC(clock).plus(resetPasswordWindow)
        user.resetPasswordToken = resetToken
        user.resetPasswordExpiry = expiry
        userRepo.save(user)
        return ResetToken(
                token = resetToken,
                expiry = nowUTC(clock).plus(resetPasswordWindow)
        )
    }

    private fun generateToken(content: String): String {
        return passwords.hashPassword(content, passwords.genSalt(pwdLogRounds))
    }

    override fun resetPassword(passwordReset: PasswordReset) {
        val userEntity = userRepo.findById(passwordReset.userID).get()
        if (passwordReset.token != userEntity.resetPasswordToken) {
            throw InvalidCredentials()
        }
        if (nowUTC(clock).isAfter(userEntity.resetPasswordExpiry)) {
            throw ExpiredToken()
        }
        val password = passwordReset.newPassword
        val pepper = passwords.genSalt(pwdLogRounds)
        val encryptedPassword = passwords.hashPassword(password, pepper)
        userEntity.encryptedPassword = encryptedPassword
        userEntity.spice.pepper = pepper
        userRepo.save(userEntity)
    }

    override fun setUserRole(userID: Long, role: Role) {
        val userEntity = userRepo.findById(userID).get()
        userEntity.role = role
        userRepo.save(userEntity)
    }
}