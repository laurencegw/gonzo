package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.TestConfig
import com.binarymonks.gonzo.core.users.api.User
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
@ContextConfiguration(classes = [TestConfig::class], loader = AnnotationConfigContextLoader::class)
class UserServiceTest {

    @Mock
    lateinit var mockClock: Clock

    @Autowired
    lateinit var userService: UserService

    @Before
    fun setUp() {
        userService.clock = mockClock
        itIsNow()
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
    }

    /**
     * Helper for setting the mock time.
     */
    private fun itIsNow(now: ZonedDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC"))): ZonedDateTime {
        Mockito.`when`(mockClock.instant()).thenReturn(now.toInstant())
        return now
    }
}