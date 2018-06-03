package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.PasswordsStub
import com.binarymonks.gonzo.core.common.ExpiredToken
import com.binarymonks.gonzo.core.common.InvalidCredentials
import com.binarymonks.gonzo.core.common.UniqueConstraintException
import com.binarymonks.gonzo.core.time.nowUTC
import com.binarymonks.gonzo.core.users.UsersConfig
import com.binarymonks.gonzo.core.users.api.PasswordReset
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.api.User
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import com.binarymonks.gonzo.userNew
import com.binarymonks.gonzo.web.GonzoDataConfig
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@RunWith(SpringRunner::class)
@ContextConfiguration(
        classes = [
            UsersConfig::class,
            GonzoDataConfig::class
        ],
        loader = AnnotationConfigContextLoader::class
)
class UserServiceTest {

    @Mock
    lateinit var mockClock: Clock

    val passwordStub = PasswordsStub()

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepo: UserRepo

    lateinit var currentNow: ZonedDateTime

    @Before
    fun setUp() {
        userService.clock = mockClock
        userService.passwords = passwordStub
        itIsNow()
        userRepo.deleteAll()
    }

    @Test
    fun createUserAndGetUser() {
        val newUser = userNew()

        val created = userService.createUser(newUser)

        val expected = User(
                id = created.id,
                nickName = newUser.nickname,
                email = newUser.email,
                role= Role.READER
        )

        Assertions.assertEquals(expected, created)
        Assertions.assertEquals(expected, userService.getUserByEmail(newUser.email))
        val userEntity = userRepo.findById(created.id).get()
        Assertions.assertEquals("${newUser.password} hashed with ${passwordStub.salt}", userEntity.encryptedPassword)
        Assertions.assertEquals(passwordStub.salt, userEntity.spice.pepper)
    }

    @Test
    fun createUser_UniqueConstraints() {
        val newUser1 = userNew()
        val duplicateNickNameUser = newUser1.copy(
                email = "2${newUser1.email}"
        )
        val duplicateEmailUser = newUser1.copy(
                nickname = "2${newUser1.nickname}"
        )
        userService.createUser(newUser1)

        try {
            userService.createUser(duplicateNickNameUser)
            Assertions.fail<String>("Should be an error")
        } catch (e: UniqueConstraintException) {
            Assertions.assertEquals(e.attributeName.toLowerCase(),"nickname")
        }

        try {
            userService.createUser(duplicateEmailUser)
            Assertions.fail<String>("Should be an error")
        } catch (e: UniqueConstraintException) {
            Assertions.assertEquals(e.attributeName.toLowerCase(),"email")
        }
    }

    @Test
    fun updateUser_UniqueConstraints() {
        val newUser1 = userNew()
        val newUser2 = newUser1.copy(
                email = "2${newUser1.email}",
                nickname = "2${newUser1.nickname}"
        )
        userService.createUser(newUser1)
        val user2 = userService.createUser(newUser2)

        try {
            userService.updateUser(user2.toUpdate().copy(
                    email = newUser1.email
            ))
            Assertions.fail<String>("Should be an error")
        } catch (e: UniqueConstraintException) {
            Assertions.assertEquals(e.attributeName.toLowerCase(),"email")
        }
    }

    @Test
    fun updateUser() {
        val newUser = userNew()

        val created = userService.createUser(newUser)

        val update = created.toUpdate().copy(
                email = "new@email.com",
                firstName = "Jane",
                lastName = "Smith"
        )

        val expected = User(
                id = update.id,
                email = update.email,
                nickName = created.nickName,
                role = Role.READER,
                firstName = update.firstName,
                lastName = update.lastName
        )

        Assertions.assertEquals(expected, userService.updateUser(update))
        Assertions.assertEquals(expected, userService.getUserByEmail(update.email))
    }

    @Test
    fun requestResetPasswordTokenAndReset() {
        val newUser = userNew().copy(password = "oldpassword")
        val created = userService.createUser(newUser)
        passwordStub.salt = "pepper2"

        val resetRequestToken = userService.requestPasswordResetToken(created.email)
        val expectedExpiryDate = nowUTC(mockClock).plus(userService.resetPasswordWindow)
        Assertions.assertEquals(expectedExpiryDate, resetRequestToken.expiry)

        val newPassword = "newpassword"

        userService.resetPassword(
                PasswordReset(
                        userID = created.id,
                        token = resetRequestToken.token,
                        newPassword = newPassword
                )
        )

        val userEntity = userRepo.findById(created.id).get()
        Assertions.assertEquals("$newPassword hashed with ${passwordStub.salt}", userEntity.encryptedPassword)
        Assertions.assertEquals(passwordStub.salt, userEntity.spice.pepper)
    }

    @Test(expected = InvalidCredentials::class)
    fun requestResetPasswordTokenAndReset_wrongToken() {
        val newUser = userNew().copy(password = "oldpassword")
        val created = userService.createUser(newUser)
        passwordStub.salt = "pepper2"

        val resetRequestToken = userService.requestPasswordResetToken(created.email)
        val expectedExpiryDate = nowUTC(mockClock).plus(userService.resetPasswordWindow)
        Assertions.assertEquals(expectedExpiryDate, resetRequestToken.expiry)

        val newPassword = "newpassword"

        userService.resetPassword(
                PasswordReset(
                        userID = created.id,
                        token = "some other token",
                        newPassword = newPassword
                )
        )
    }

    @Test(expected = ExpiredToken::class)
    fun requestResetPasswordTokenAndReset_expiredToken() {
        val newUser = userNew().copy(password = "oldpassword")
        val created = userService.createUser(newUser)
        passwordStub.salt = "pepper2"


        val resetRequestToken = userService.requestPasswordResetToken(created.email)

        itIsNow(currentNow.plus(userService.resetPasswordWindow).plusMinutes(1))

        val newPassword = "newpassword"

        userService.resetPassword(
                PasswordReset(
                        userID = created.id,
                        token = resetRequestToken.token,
                        newPassword = newPassword
                )
        )
    }

    @Test
    fun setUserRole(){
        val newUser = userNew()

        val created = userService.createUser(newUser)
        Assertions.assertEquals(Role.READER, created.role)

        userService.setUserRole(created.id, Role.ADMIN)

        val retrieved = userService.getUserByEmail(created.email)
        Assertions.assertEquals(Role.ADMIN, retrieved.role)
    }


    /**
     * TODO: Password reset considerations:
     *
     *  - Do not expose email address in token
     *  - email layer should send an email regardless of the user existing or not
     *  - captchas
     *  - logging failed attempts
     *  - Personal questions before reset sent
     *
     */


    /**
     * Helper for setting the mock time.
     */
    private fun itIsNow(now: ZonedDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC"))): ZonedDateTime {
        currentNow = now
        Mockito.`when`(mockClock.instant()).thenReturn(now.toInstant())
        return now
    }
}