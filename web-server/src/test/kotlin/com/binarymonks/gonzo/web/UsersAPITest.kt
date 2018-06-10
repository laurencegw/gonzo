package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.clients.UserClient
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.test.GonzoTestHarnessConfig
import com.binarymonks.gonzo.core.test.harness.TestDataManager
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.api.UserRoleUpdate
import com.binarymonks.gonzo.newUser
import org.junit.jupiter.api.*
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [GonzoApplication::class, GonzoTestHarnessConfig::class]
)
@ExtendWith(SpringExtension::class)
class UsersAuthorizedPermissionsTest {

    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var testDataManager: TestDataManager

    lateinit var userClient: UserClient

    @BeforeEach
    fun setUp() {
        userClient = UserClient("http://localhost:$port")
    }

    @TestFactory
    fun createUser(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create user", Role.READER, false),
                arrayOf("Author cannot create user", Role.AUTHOR, false),
                arrayOf("Admin can create user", Role.ADMIN, true)
        ).map {
            dynamicTest(it[0] as String, {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                testDataManager.forceCreateUser(requestUser, userRole)
                userClient.signIn(requestUser.email, requestUser.password)

                if (allowed) {
                    userClient.createUser(newUser().copy(
                            email = "another@blah.com",
                            handle = "Mike"
                    ))
                } else {
                    try {
                        userClient.createUser(newUser().copy(
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

    @TestFactory
    fun setUserRole(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader set roles", Role.READER, false),
                arrayOf("Author set roles", Role.AUTHOR, false),
                arrayOf("Admin can set roles", Role.ADMIN, true)
        ).map {
            dynamicTest(it[0] as String, {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean
                val requestingUser = newUser()
                testDataManager.forceCreateUser(requestingUser, userRole)
                userClient.signIn(requestingUser.email, requestingUser.password)

                val originalRole = Role.READER
                val newRole = Role.ADMIN

                val targetUser = testDataManager.forceCreateUser(newUser().copy(
                        email = "another.blah.com",
                        handle = "another"
                ), originalRole)

                if (allowed) {
                    userClient.setUserRole(UserRoleUpdate(targetUser.id, newRole))
                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        userClient.setUserRole(UserRoleUpdate(targetUser.id, newRole))
                    })
                }
            })
        }.toList()
    }

    @TestFactory
    fun updateOtherUser(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader update another user", Role.READER, false),
                arrayOf("Author update another user", Role.AUTHOR, false),
                arrayOf("Admin update another user", Role.ADMIN, false)
        ).map {
            dynamicTest(it[0] as String, {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean
                val requestingUser = newUser()
                testDataManager.forceCreateUser(requestingUser, userRole)
                userClient.signIn(requestingUser.email, requestingUser.password)


                val targetUser = testDataManager.forceCreateUser(newUser().copy(
                        email = "another.blah.com",
                        handle = "another"
                ))

                if (allowed) {
                    userClient.updateUser(targetUser.toUpdate().copy(
                            email = "changed" + targetUser.email
                    ))
                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        userClient.updateUser(targetUser.toUpdate().copy(
                                email = "changed" + targetUser.email
                        ))
                    })
                }
            })
        }.toList()
    }


    @TestFactory
    fun updateOwnUser(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader update own user", Role.READER, true),
                arrayOf("Author update own user", Role.AUTHOR, true),
                arrayOf("Admin update own user", Role.ADMIN, true)
        ).map {
            dynamicTest(it[0] as String, {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean
                val requestingUser = newUser()
                val user = testDataManager.forceCreateUser(requestingUser, userRole)
                userClient.signIn(requestingUser.email, requestingUser.password)


                if (allowed) {
                    userClient.updateUser(user.toUpdate().copy(
                            email = "changed" + user.email
                    ))
                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        userClient.updateUser(user.toUpdate().copy(
                                email = "changed" + user.email
                        ))
                    })
                }
            })
        }.toList()
    }
}

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [GonzoApplication::class, GonzoTestHarnessConfig::class]
)
@ExtendWith(SpringExtension::class)
class TestUsers {

    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var testDataManager: TestDataManager

    lateinit var userClient: UserClient

    @BeforeEach
    fun setUp() {
        userClient = UserClient("http://localhost:$port")
        testDataManager.clearData()
    }

    @Test
    fun getUserWithCredentials() {
        val newUser = newUser()

        val expected = testDataManager.forceCreateUser(com.binarymonks.gonzo.newUser())

        userClient.signIn(newUser.email, newUser.password)

        val actual = userClient.getUser()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @Disabled("Need To do more work here")
    fun requestResetEmailAndReset(){

    }


}

