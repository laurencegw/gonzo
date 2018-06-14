package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.blogEntryDraft
import com.binarymonks.gonzo.blogEntryNew
import com.binarymonks.gonzo.clients.BlogClient
import com.binarymonks.gonzo.core.blog.service.BlogService
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.test.GonzoTestHarnessConfig
import com.binarymonks.gonzo.core.test.harness.TestDataManager
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.api.UserRoleUpdate
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import com.binarymonks.gonzo.core.users.service.UserService
import com.binarymonks.gonzo.newUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.reset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
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

    @MockBean
    lateinit var blogServiceMock: BlogService

    @Autowired
    lateinit var testDataManager: TestDataManager

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
                reset(blogServiceMock)
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                testDataManager.forceCreateUser(requestUser, userRole)
                blogClient.signIn(requestUser.email, requestUser.password)

                if (allowed) {
                    val newBlogEntry = blogEntryNew()
                    val expectedBlogEntry = blogEntryDraft()
                    Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(expectedBlogEntry)
                    blogClient.createBlogEntry(blogEntryNew())
                    Mockito.verify(blogServiceMock).createBlogEntry(newBlogEntry)

                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        blogClient.createBlogEntry(blogEntryNew())
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
                reset(blogServiceMock)
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                testDataManager.forceCreateUser(requestUser, userRole)
                blogClient.signIn(requestUser.email, requestUser.password)

                val targetUser = testDataManager.forceCreateUser(
                        newUser().copy(email="another@email.com", handle = "another")
                )

                if (allowed) {
                    val newBlogEntry = blogEntryNew().copy(authorID = targetUser.id)
                    val expectedBlogEntry = blogEntryDraft()
                    Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(expectedBlogEntry)
                    blogClient.createBlogEntry(blogEntryNew())
                    Mockito.verify(blogServiceMock).createBlogEntry(newBlogEntry)

                } else {
                    Assertions.assertThrows(NotAuthorized::class.java, {
                        blogClient.createBlogEntry(blogEntryNew())
                    })
                }

            })
        }.toList()
    }

//    @Test()
//    @Disabled("Not authorizing yet")
//    fun createBlogEntry_notSignedIn() {
//        val newBlogEntry = blogEntryNew()
//        Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(blogEntryDraft())
//        Assertions.assertThrows(NotAuthentic::class.java, { blogClient.createBlogEntry(newBlogEntry) })
//
//    }
//
//    @Test()
//    fun createBlogEntry() {
//        login()
//
//        val newBlogEntry = blogEntryNew()
//        val expectedBlogEntry = blogEntryDraft()
//        Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(expectedBlogEntry)
//
//        val actual = blogClient.createBlogEntry(newBlogEntry)
//
//        Mockito.verify(blogServiceMock).createBlogEntry(newBlogEntry)
//        Assertions.assertEquals(expectedBlogEntry, actual)
//    }
//
//    private fun login() {
//        val newUser = newUser()
//        userService.createUser(newUser)
//        blogClient.signIn(newUser.email, newUser.password)
//    }
//
//    @Test
//    fun updateBlogEntry() {
//        login()
//
//        val blogEntryUpdate = blogEntryUpdate()
//        val expectedBlogEntry = blogEntryDraft()
//        Mockito.`when`(blogServiceMock.updateBlogEntry(blogEntryUpdate)).thenReturn(expectedBlogEntry)
//
//        val actual = blogClient.updateBlogEntry(blogEntryUpdate)
//
//        Mockito.verify(blogServiceMock).updateBlogEntry(blogEntryUpdate)
//        Assertions.assertEquals(expectedBlogEntry, actual)
//    }
//
//    @Test()
//    @Disabled("Does not go through authentication yet")
//    fun updateBlogEntry_notSignedIn() {
//        val blogEntryUpdate = blogEntryUpdate()
//        Mockito.`when`(blogServiceMock.updateBlogEntry(blogEntryUpdate)).thenReturn(blogEntryDraft())
//
//        Assertions.assertThrows(NotAuthentic::class.java, {
//            blogClient.updateBlogEntry(blogEntryUpdate)
//        })
//    }
//
//    @Test
//    fun getBlogEntryHeaders() {
//        val expectedHeaders = listOf(
//                blogEntryHeader().copy(id = 1),
//                blogEntryHeader().copy(id = 2)
//        )
//
//        Mockito.`when`(blogServiceMock.getBlogEntryHeaders()).thenReturn(expectedHeaders)
//
//        val actual = blogClient.getBlogEntryHeaders()
//
//        Mockito.verify(blogServiceMock).getBlogEntryHeaders()
//        Assertions.assertEquals(expectedHeaders, actual)
//    }
//
//    @Test
//    fun getBlogEntryById() {
//        val expected = blogEntry()
//        Mockito.`when`(blogServiceMock.getBlogEntryById(expected.id)).thenReturn(expected)
//
//        val actual = blogClient.getBlogEntryById(expected.id)
//
//        Mockito.verify(blogServiceMock).getBlogEntryById(expected.id)
//        Assertions.assertEquals(expected, actual)
//    }

}