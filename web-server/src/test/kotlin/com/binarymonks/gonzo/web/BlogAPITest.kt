package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.blogEntryNew
import com.binarymonks.gonzo.blogEntryUpdate
import com.binarymonks.gonzo.clients.BlogClient
import com.binarymonks.gonzo.core.blog.service.BlogService
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.test.GonzoTestHarnessConfig
import com.binarymonks.gonzo.core.test.harness.TestDataManager
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.newUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
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
class BlogAPITest {

    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var testDataManager: TestDataManager

    @Autowired
    lateinit var blogService: BlogService

    lateinit var blogClient: BlogClient

    @BeforeEach
    fun setUp() {
        blogClient = BlogClient("http://localhost:$port")
    }

    @TestFactory
    fun createBlog(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create blog", Role.READER, false),
                arrayOf("Author can create blog", Role.AUTHOR, true),
                arrayOf("Admin can create blog", Role.ADMIN, true)
        ).map {
            DynamicTest.dynamicTest(it[0] as String, {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                val user = testDataManager.forceCreateUser(requestUser, userRole)
                blogClient.signIn(requestUser.email, requestUser.password)

                val newBlog = blogEntryNew().copy(authorID = user.id)

                if (allowed) {
                    blogClient.createBlogEntry(newBlog)
                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        blogClient.createBlogEntry(newBlog)
                    })
                }

            })
        }.toList()
    }

    @TestFactory
    fun createBlog_forSomeoneElse(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create blog", Role.READER, false),
                arrayOf("Author can create blog", Role.AUTHOR, false),
                arrayOf("Admin can create blog", Role.ADMIN, false)
        ).map {
            DynamicTest.dynamicTest(it[0] as String, {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                testDataManager.forceCreateUser(requestUser, userRole)
                blogClient.signIn(requestUser.email, requestUser.password)

                val targetUser = testDataManager.forceCreateUser(
                        newUser().copy(email = "another@email.com", handle = "another")
                )

                if (allowed) {
                    val newBlogEntry = blogEntryNew().copy(authorID = targetUser.id)
                    blogClient.createBlogEntry(newBlogEntry)
                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        blogClient.createBlogEntry(blogEntryNew())
                    })
                }

            })
        }.toList()
    }

    @TestFactory
    fun updateBlog_whenItIsYourOwn(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader can update own blog", Role.READER, true),
                arrayOf("Author can update own blog", Role.AUTHOR, true),
                arrayOf("Admin can update own blog", Role.ADMIN, true)
        ).map {
            DynamicTest.dynamicTest(it[0] as String, {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                val user = testDataManager.forceCreateUser(requestUser, userRole)
                blogClient.signIn(requestUser.email, requestUser.password)

                val newBlog = blogEntryNew().copy(authorID = user.id)
                val myBlog = blogService.createBlogEntry(newBlog)

                val update = blogEntryUpdate().copy(id = myBlog.id)

                if (allowed) {
                    blogClient.updateBlogEntry(update)

                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        blogClient.updateBlogEntry(update)
                    })
                }
            })
        }.toList()
    }

    @TestFactory
    fun updateBlog_forSomeoneElse(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create blog", Role.READER, false),
                arrayOf("Author can create blog", Role.AUTHOR, false),
                arrayOf("Admin can create blog", Role.ADMIN, false)
        ).map {
            DynamicTest.dynamicTest(it[0] as String, {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                testDataManager.forceCreateUser(requestUser, userRole)
                blogClient.signIn(requestUser.email, requestUser.password)

                val targetUser = testDataManager.forceCreateUser(
                        newUser().copy(email = "another@email.com", handle = "another")
                )

                val newBlog = blogEntryNew().copy(authorID = targetUser.id)
                val myBlog = blogService.createBlogEntry(newBlog)

                val update = blogEntryUpdate().copy(id = myBlog.id)

                if (allowed) {
                    blogClient.updateBlogEntry(update)

                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        blogClient.updateBlogEntry(update)
                    })
                }

            })
        }.toList()
    }

}