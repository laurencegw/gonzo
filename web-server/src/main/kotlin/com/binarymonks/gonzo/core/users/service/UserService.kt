package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.core.common.ExpiredToken
import com.binarymonks.gonzo.core.common.InvalidCredentials
import com.binarymonks.gonzo.core.common.UniqueConstraintException
import com.binarymonks.gonzo.core.email.api.Emails
import com.binarymonks.gonzo.core.email.api.ResetPasswordEmail
import com.binarymonks.gonzo.core.time.nowUTC
import com.binarymonks.gonzo.core.users.api.*
import com.binarymonks.gonzo.core.users.persistence.Spice
import com.binarymonks.gonzo.core.users.persistence.UserEntity
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class UserService : Users {

    @Autowired
    lateinit var userRepo: UserRepo
    @Autowired
    lateinit var passwords: Passwords
    @Autowired
    lateinit var emails: Emails

    var resetPasswordWindow: Duration = Duration.ofHours(1)

    @Value("\${bcrypt.logrounds}")
    var pwdLogRounds: Int = 10

    override fun createUser(user: UserNew): User {
        if (userRepo.findByHandle(user.handle).isPresent) {
            throw UniqueConstraintException("handle")
        }
        if (userRepo.findByEmail(user.email).isPresent) {
            throw UniqueConstraintException("email")
        }
        val password = user.password
        val pepper = passwords.genSalt(pwdLogRounds)
        val encryptedPassword = passwords.hashPassword(password, pepper)
        val userEntity = UserEntity(
                email = user.email,
                handle = user.handle,
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

    private fun requestPasswordResetToken(user: UserEntity): ResetToken {
        val resetToken = generateToken(user.email)
        val expiry = nowUTC().plus(resetPasswordWindow)
        user.resetPasswordToken = resetToken
        user.resetPasswordExpiry = expiry
        userRepo.save(user)
        return ResetToken(
                token = resetToken,
                expiry = nowUTC().plus(resetPasswordWindow)
        )
    }

    override fun requestPasswordResetEmail(email: String) {
        val userRecord = userRepo.findByEmail(email)
        if (userRecord.isPresent) {
            val token = requestPasswordResetToken(userRecord.get())
            emails.sendResetPassword(ResetPasswordEmail(
                    emailAddress = email,
                    resetToken = token
            ))
        } else {
            emails.sendUnknownUserPasswordReset(email)
        }
    }

    private fun generateToken(content: String): String {
        return passwords.hashPassword(content, passwords.genSalt(pwdLogRounds))
    }

    override fun resetPassword(passwordReset: PasswordReset) {
        val userEntity = userRepo.findById(passwordReset.userID).get()
        if (passwordReset.token != userEntity.resetPasswordToken) {
            throw InvalidCredentials()
        }
        if (nowUTC().isAfter(userEntity.resetPasswordExpiry)) {
            throw ExpiredToken()
        }
        val password = passwordReset.newPassword
        val pepper = passwords.genSalt(pwdLogRounds)
        val encryptedPassword = passwords.hashPassword(password, pepper)
        userEntity.encryptedPassword = encryptedPassword
        userEntity.spice.pepper = pepper
        userRepo.save(userEntity)
    }

    override fun setUserRole(userRoleUpdate: UserRoleUpdate) {
        val userEntity = userRepo.findById(userRoleUpdate.id).get()
        userEntity.role = userRoleUpdate.role
        userRepo.save(userEntity)
    }
}