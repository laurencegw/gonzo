package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.blogEntryNew
import com.binarymonks.gonzo.blogEntryUpdate
import com.binarymonks.gonzo.clients.BlogClient
import com.binarymonks.gonzo.core.blog.service.BlogService
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.common.ValidationException
import com.binarymonks.gonzo.core.common.ValidationMessage
import com.binarymonks.gonzo.core.test.GonzoTestHarnessConfig
import com.binarymonks.gonzo.core.test.harness.TestDataManager
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.newUser
import org.junit.jupiter.api.*
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
            DynamicTest.dynamicTest(it[0] as String) {
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
                    Assertions.assertThrows(NotAuthorized::class.java) {
                        blogClient.createBlogEntry(newBlog)
                    }
                }

            }
        }.toList()
    }

    @Test
    fun createBlogValidation(){
        testDataManager.clearData()
        val requestUser = newUser()
        val user = testDataManager.forceCreateUser(requestUser, Role.ADMIN)
        blogClient.signIn(requestUser.email, requestUser.password)

        val newBlog = blogEntryNew().copy(
                title = "",
                authorID = user.id
        )

        val exception = assertThrows<ValidationException> {
            blogClient.createBlogEntry(newBlog)
        }
        Assertions.assertEquals(
                listOf(
                        ValidationMessage(
                                "title",
                                "Title is required"
                        )
                )
                , exception.validationMessages.messages
        )
    }

    @TestFactory
    fun createBlog_forSomeoneElse(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create blog", Role.READER, false),
                arrayOf("Author can create blog", Role.AUTHOR, false),
                arrayOf("Admin can create blog", Role.ADMIN, false)
        ).map {
            DynamicTest.dynamicTest(it[0] as String) {
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
                    Assertions.assertThrows(NotAuthorized::class.java) {
                        blogClient.createBlogEntry(blogEntryNew())
                    }
                }

            }
        }.toList()
    }

    @TestFactory
    fun updatePublishAndReadBlogDraft_whenItIsYourOwn(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader can update own blog", Role.READER),
                arrayOf("Author can update own blog", Role.AUTHOR),
                arrayOf("Admin can update own blog", Role.ADMIN)
        ).map {
            DynamicTest.dynamicTest(it[0] as String) {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role

                val requestUser = newUser()
                val user = testDataManager.forceCreateUser(requestUser, userRole)
                blogClient.signIn(requestUser.email, requestUser.password)

                val newBlog = blogEntryNew().copy(authorID = user.id)
                val myBlog = blogService.createBlogEntry(newBlog)

                val update = blogEntryUpdate().copy(id = myBlog.id)
                blogClient.updateBlogEntry(update)
                blogClient.publishBlogEntry(myBlog.id)
                blogClient.getBlogEntryDraftByID(myBlog.id)
                blogClient.getBlogEntryDraftHeaders(user.id)
            }
        }.toList()
    }

    @TestFactory
    fun updatePublishAndReadSomeoneElsesBlogDraft(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot update someone else's blog", Role.READER),
                arrayOf("Author cannot update someone else's blog", Role.AUTHOR),
                arrayOf("Admin cannot update someone else's blog", Role.ADMIN)
        ).map {
            DynamicTest.dynamicTest(it[0] as String) {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role

                val requestUser = newUser()
                testDataManager.forceCreateUser(requestUser, userRole)
                blogClient.signIn(requestUser.email, requestUser.password)

                val targetUser = testDataManager.forceCreateUser(
                        newUser().copy(email = "another@email.com", handle = "another")
                )

                val newBlog = blogEntryNew().copy(authorID = targetUser.id)
                val someOneElseBlog = blogService.createBlogEntry(newBlog)
                blogService.publishBlogEntry(someOneElseBlog.id)

                val update = blogEntryUpdate().copy(id = someOneElseBlog.id)
                Assertions.assertThrows(NotAuthorized::class.java) {
                    blogClient.updateBlogEntry(update)
                }
                Assertions.assertThrows(NotAuthorized::class.java) {
                    blogClient.publishBlogEntry(someOneElseBlog.id)
                }
                Assertions.assertThrows(NotAuthorized::class.java) {
                    blogClient.getBlogEntryDraftByID(someOneElseBlog.id)
                }
                Assertions.assertThrows(NotAuthorized::class.java) {
                    blogClient.getBlogEntryDraftHeaders(targetUser.id)
                }
            }
        }.toList()
    }

    @Test
    fun anyoneCanReadAnyonesPublishedBlogs() {
        testDataManager.clearData()

        val targetUser = testDataManager.forceCreateUser(
                newUser().copy(email = "another@email.com", handle = "another")
        )

        val otherNewBlog = blogEntryNew().copy(authorID = targetUser.id)
        val otherBlog = blogService.createBlogEntry(otherNewBlog)
        blogService.publishBlogEntry(otherBlog.id)


        blogClient.getBlogEntryById(otherBlog.id)
        blogClient.getBlogEntryHeaders()
        blogClient.getBlogEntryHeadersByAuthor(targetUser.id)

    }

}