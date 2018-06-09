package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.clients.UserClient
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import com.binarymonks.gonzo.core.users.service.UserService
import com.binarymonks.gonzo.userNew
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.junit4.rules.SpringMethodRule
import org.springframework.test.context.junit4.rules.SpringClassRule
import org.junit.ClassRule
import org.junit.Rule
import org.junit.runners.Parameterized


@RunWith(SpringRunner::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [GonzoApplication::class]
)
class UsersAuthorizedTest {

    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var userRepo: UserRepo

    lateinit var userClient: UserClient

    @ClassRule
    val SPRING_CLASS_RULE = SpringClassRule()

    @Rule
    val springMethodRule = SpringMethodRule()

    @Before
    fun setUp() {
        userClient = UserClient("http://localhost:$port")
    }

    @Test
    fun createUser() {
        listOf(
                arrayOf(Role.READER, false),
                arrayOf(Role.AUTHOR, false),
                arrayOf(Role.ADMIN, true)
        ).map { createUserScenario(it[0] as Role, it[1] as Boolean) }
    }

    @Test
    @Parameterized.Parameters
    fun createUserScenario(userRole: Role, allowed: Boolean) {
        userRepo.deleteAll()
        val newUser = userNew()
        val user = userService.createUser(newUser)
        userService.setUserRole(user.id, userRole)
        userClient.signIn(newUser.email, newUser.password)
        if (allowed) {
            userClient.createUser(userNew().copy(
                    email = "another@blah.com",
                    handle = "Mike"
            ))
        } else {
            try {
                userClient.createUser(userNew().copy(
                        email = "another@blah.com",
                        handle = "Mike"
                ))
                Assertions.fail<String>("Should not be allowed")
            } catch (e: NotAuthorized) {
            }
        }
    }

}