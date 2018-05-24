package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.PasswordsStub
import com.binarymonks.gonzo.TestConfig
import com.binarymonks.gonzo.core.common.InvalidToken
import com.binarymonks.gonzo.core.time.nowUTC
import com.binarymonks.gonzo.core.users.api.PasswordReset
import com.binarymonks.gonzo.core.users.api.User
import com.binarymonks.gonzo.core.users.api.PasswordUpdate
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import com.binarymonks.gonzo.userNew
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
        classes = [TestConfig::class],
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
                email = newUser.email
        )

        Assertions.assertEquals(expected, created)
        Assertions.assertEquals(expected,userService.getUserByEmail(newUser.email))
        val userEntity = userRepo.findById(created.id).get()
        Assertions.assertEquals("${newUser.password} hashed with ${passwordStub.salt}", userEntity.encryptedPassword)
        Assertions.assertEquals(passwordStub.salt, userEntity.spice.pepper)
    }

    @Test
    fun updateUser(){
        val newUser = userNew()

        val created = userService.createUser(newUser)

        val update = created.toUpdate().copy(
                email = "new@email.com",
                firstName = "Jane",
                lastName = "Smith"
        )

        val expected = User(
                id=update.id,
                email = update.email,
                firstName = update.firstName,
                lastName = update.lastName
        )

        Assertions.assertEquals(expected, userService.updateUser(update))
        Assertions.assertEquals(expected, userService.getUserByEmail(update.email))
    }

    @Test
    fun updatePassword(){
        //TODO: Maybe remove this functionalit completely?
        val newUser = userNew().copy(password = "oldpassword")
        val created = userService.createUser(newUser)

        passwordStub.salt="pepper2"

        userService.updatePassword(PasswordUpdate(
                id=created.id,
                newPassword = "newpassword"
        ))

        val userEntity = userRepo.findById(created.id).get()
        Assertions.assertEquals("newpassword hashed with pepper2", userEntity.encryptedPassword)
        Assertions.assertEquals("pepper2", userEntity.spice.pepper)
    }

    @Test
    fun requestResetPasswordTokenAndReset(){
        val newUser = userNew().copy(password = "oldpassword")
        val created = userService.createUser(newUser)
        passwordStub.salt="pepper2"

        val resetRequestToken = userService.requestPasswordResetToken(created.id)
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

    @Test(expected = InvalidToken::class)
    fun requestResetPasswordTokenAndReset_wrongToken(){
        val newUser = userNew().copy(password = "oldpassword")
        val created = userService.createUser(newUser)
        passwordStub.salt="pepper2"

        val resetRequestToken = userService.requestPasswordResetToken(created.id)
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
        Mockito.`when`(mockClock.instant()).thenReturn(now.toInstant())
        return now
    }
}