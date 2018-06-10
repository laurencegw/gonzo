package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.clients.UserClient
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.test.GonzoTestHarnessConfig
import com.binarymonks.gonzo.core.test.harness.TestDataManager
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.api.UserRoleUpdate
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
        classes = [GonzoApplication::class, GonzoTestHarnessConfig::class]
)
@ExtendWith(SpringExtension::class)
class UsersAuthorizedTest {

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
    fun createUserPermissions(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create user", Role.READER, false),
                arrayOf("Author cannot create user", Role.AUTHOR, false),
                arrayOf("Admin can create user", Role.ADMIN, true)
        ).map {
            dynamicTest(it[0] as String,{
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = userNew()
                testDataManager.forceCreateUser(requestUser, userRole)
                userClient.signIn(requestUser.email, requestUser.password)

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

    @TestFactory
    fun setUserRolePermissions(): List<DynamicTest>{
        return listOf(
                arrayOf("Reader set roles", Role.READER, false),
                arrayOf("Author set roles", Role.AUTHOR, false),
                arrayOf("Admin can set roles", Role.ADMIN, true)
        ).map {
            dynamicTest(it[0] as String,{
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean
                val requestingUser = userNew()
                testDataManager.forceCreateUser(requestingUser, userRole)
                userClient.signIn(requestingUser.email, requestingUser.password)

                val originalRole = Role.READER
                val newRole = Role.ADMIN

                val targetUser = testDataManager.forceCreateUser(userNew().copy(
                        email = "another.blah.com",
                        handle = "another"
                ), originalRole)

                if (allowed) {
                    userClient.setUserRole(UserRoleUpdate(targetUser.id, newRole))
                } else {
                    try {
                        userClient.setUserRole(UserRoleUpdate(targetUser.id, newRole))
                        Assertions.fail<String>("Should not be allowed")
                    } catch (e: NotAuthorized) {
                    }
                }
            })
        }.toList()
    }

}