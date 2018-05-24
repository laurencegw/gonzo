package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.PasswordsStub
import com.binarymonks.gonzo.TestConfig
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
        Assertions.assertEquals("${newUser.password} hashed with pepper", userEntity.encryptedPassword)
        Assertions.assertEquals("pepper", userEntity.spice.pepper)
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
        passwordStub.salt="pepper2"
        val newUser = userNew().copy(password = "oldpassword")

        val created = userService.createUser(newUser)

        userService.updatePassword(PasswordUpdate(
                id=created.id,
                newPassword = "newpassword"
        ))

        val userEntity = userRepo.findById(created.id).get()
        Assertions.assertEquals("newpassword hashed with pepper2", userEntity.encryptedPassword)
        Assertions.assertEquals("pepper2", userEntity.spice.pepper)
    }

    @Test
    fun requestResetPasswordTokenReset(){
        passwordStub.salt="pepper2"
        val newUser = userNew().copy(password = "oldpassword")

        val created = userService.createUser(newUser)

        val resetRequestToken = userService.requestPasswordResetToken(created.email)
        val newPassword = "newpassword"

        userService.resetPassword()
    }

    /**
     * Helper for setting the mock time.
     */
    private fun itIsNow(now: ZonedDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC"))): ZonedDateTime {
        Mockito.`when`(mockClock.instant()).thenReturn(now.toInstant())
        return now
    }
}