package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.PasswordsStub
import com.binarymonks.gonzo.core.common.ExpiredToken
import com.binarymonks.gonzo.core.common.InvalidCredentials
import com.binarymonks.gonzo.core.test.GonzoTestConfig
import com.binarymonks.gonzo.core.test.TestDataManager
import com.binarymonks.gonzo.core.time.clock
import com.binarymonks.gonzo.core.users.api.LoginCredentials
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
        classes = [
            GonzoTestConfig::class
        ],
        loader = AnnotationConfigContextLoader::class
)
class SignInServiceTest {

    @Mock
    lateinit var mockClock: Clock

    private val passwordStub = PasswordsStub()

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var signInService: SignInService

    @Autowired
    lateinit var testDataManager: TestDataManager

    lateinit var currentNow: ZonedDateTime

    @Before
    fun setUp() {
        clock = mockClock
        userService.passwords = passwordStub
        signInService.passwords = passwordStub
        itIsNow()
        testDataManager.clearData()
    }

    @Test
    fun loginAndCheckLoggedIn() {
        val newUser = userNew()
        val expectedUser = userService.createUser(userNew())

        val token = signInService.login(LoginCredentials(
                email = newUser.email,
                password = newUser.password
        ))

        signInService.assertLoggedIn(token)
        Assertions.assertEquals(expectedUser, signInService.getUserFromToken(token))
    }

    @Test(expected = InvalidCredentials::class)
    fun loginWrongPassword() {
        val newUser = userNew()
        userService.createUser(userNew())
        signInService.login(LoginCredentials(
                email = newUser.email,
                password = newUser.password + "1"
        ))
    }

    @Test
    fun loginCheckLoggedIn_BadToken() {
        val newUser = userNew()
        userService.createUser(userNew())

        val token = signInService.login(LoginCredentials(
                email = newUser.email,
                password = newUser.password
        ))
        try {
            signInService.assertLoggedIn(token + "k")
            Assertions.fail<String>("Should have had an error")
        }catch(e:InvalidCredentials){}
        try{
            signInService.getUserFromToken(token+"g")
            Assertions.fail<String>("Should have had an error")
        }catch (e:InvalidCredentials){}
    }

    @Test
    fun loginCheckLoggedIn_Expired() {
        val newUser = userNew()
        userService.createUser(userNew())

        val token = signInService.login(LoginCredentials(
                email = newUser.email,
                password = newUser.password
        ))

        itIsNow(currentNow.plus(signInService.tokenLifeSpan).minusMinutes(1))
        signInService.assertLoggedIn(token)
        itIsNow(currentNow.plus(signInService.tokenLifeSpan).plusMinutes(2))
        try {
            signInService.assertLoggedIn(token)
            Assertions.fail<String>("Should have had an error")
        }catch(e:ExpiredToken){}
        try{
            signInService.getUserFromToken(token)
            Assertions.fail<String>("Should have had an error")
        }catch (e:ExpiredToken){}
    }

    /**
     * Helper for setting the mock time.
     */
    private fun itIsNow(now: ZonedDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC"))): ZonedDateTime {
        currentNow = now
        Mockito.`when`(mockClock.instant()).thenReturn(now.toInstant())
        return now
    }
}