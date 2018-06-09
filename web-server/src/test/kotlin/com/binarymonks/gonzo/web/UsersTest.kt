package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.clients.UserClient
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import com.binarymonks.gonzo.core.users.service.UserService
import com.binarymonks.gonzo.userNew
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [GonzoApplication::class]
)
@ExtendWith(SpringExtension::class)
class UsersAuthorizedTest {

    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var userRepo: UserRepo


    @BeforeEach
    fun setUp() {

    }

    @TestFactory
    fun createUserPermissions(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create user", Role.READER, false),
                arrayOf("Author cannot create user", Role.AUTHOR, false),
                arrayOf("Admin can create user", Role.ADMIN, true)
        ).map {
            dynamicTest(it[0] as String,{
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean
                val userClient = UserClient("http://localhost:$port")
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
            })
        }.toList()
    }

}