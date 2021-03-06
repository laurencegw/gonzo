package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.articleEntryNew
import com.binarymonks.gonzo.articleEntryUpdate
import com.binarymonks.gonzo.clients.ArticlesClient
import com.binarymonks.gonzo.core.articles.service.ArticlesService
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
class ArticlesAPITest {

    @LocalServerPort
    var port: Int = -1

    @Autowired
    lateinit var testDataManager: TestDataManager

    @Autowired
    lateinit var articleService: ArticlesService

    lateinit var articleClient: ArticlesClient

    @BeforeEach
    fun setUp() {
        articleClient = ArticlesClient("http://localhost:$port")
    }

    @TestFactory
    fun createArticle(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create articles", Role.READER, false),
                arrayOf("Author can create articles", Role.AUTHOR, true),
                arrayOf("Admin can create articles", Role.ADMIN, true)
        ).map {
            DynamicTest.dynamicTest(it[0] as String) {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                val user = testDataManager.forceCreateUser(requestUser, userRole)
                articleClient.signIn(requestUser.email, requestUser.password)

                val newArticle = articleEntryNew().copy(authorID = user.id)

                if (allowed) {
                    articleClient.createArticle(newArticle)
                } else {
                    Assertions.assertThrows(NotAuthorized::class.java) {
                        articleClient.createArticle(newArticle)
                    }
                }

            }
        }.toList()
    }

    @Test
    fun createArticleValidation(){
        testDataManager.clearData()
        val requestUser = newUser()
        val user = testDataManager.forceCreateUser(requestUser, Role.ADMIN)
        articleClient.signIn(requestUser.email, requestUser.password)

        val newArticle = articleEntryNew().copy(
                title = "",
                authorID = user.id
        )

        val exception = assertThrows<ValidationException> {
            articleClient.createArticle(newArticle)
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
    fun createArticle_forSomeoneElse(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot create articles", Role.READER, false),
                arrayOf("Author can create articles", Role.AUTHOR, false),
                arrayOf("Admin can create articles", Role.ADMIN, false)
        ).map {
            DynamicTest.dynamicTest(it[0] as String) {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role
                val allowed: Boolean = it[2] as Boolean

                val requestUser = newUser()
                testDataManager.forceCreateUser(requestUser, userRole)
                articleClient.signIn(requestUser.email, requestUser.password)

                val targetUser = testDataManager.forceCreateUser(
                        newUser().copy(email = "another@email.com", handle = "another")
                )

                if (allowed) {
                    val newArticleEntry = articleEntryNew().copy(authorID = targetUser.id)
                    articleClient.createArticle(newArticleEntry)
                } else {
                    Assertions.assertThrows(NotAuthorized::class.java) {
                        articleClient.createArticle(articleEntryNew())
                    }
                }

            }
        }.toList()
    }

    @TestFactory
    fun updatePublishReadDeleteArticleDraft_whenItIsYourOwn(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader can update own articles", Role.READER),
                arrayOf("Author can update own articles", Role.AUTHOR),
                arrayOf("Admin can update own articles", Role.ADMIN)
        ).map {
            DynamicTest.dynamicTest(it[0] as String) {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role

                val requestUser = newUser()
                val user = testDataManager.forceCreateUser(requestUser, userRole)
                articleClient.signIn(requestUser.email, requestUser.password)

                val newArticle = articleEntryNew().copy(authorID = user.id)
                val myArticle = articleService.createArticle(newArticle)

                val update = articleEntryUpdate().copy(id = myArticle.id)
                articleClient.updateArticle(update)
                articleClient.publishArticle(myArticle.id)
                articleClient.getArticleDraftByID(myArticle.id)
                articleClient.getArticleDraftHeaders(user.id)
                articleClient.deleteArticle(myArticle.id)
            }
        }.toList()
    }

    @TestFactory
    fun update_Publish_Read_Delete_SomeoneElsesArticleDraft(): List<DynamicTest> {
        return listOf(
                arrayOf("Reader cannot update someone else's articles", Role.READER),
                arrayOf("Author cannot update someone else's articles", Role.AUTHOR),
                arrayOf("Admin cannot update someone else's articles", Role.ADMIN)
        ).map {
            DynamicTest.dynamicTest(it[0] as String) {
                testDataManager.clearData()
                val userRole: Role = it[1] as Role

                val requestUser = newUser()
                testDataManager.forceCreateUser(requestUser, userRole)
                articleClient.signIn(requestUser.email, requestUser.password)

                val targetUser = testDataManager.forceCreateUser(
                        newUser().copy(email = "another@email.com", handle = "another")
                )

                val newArticle = articleEntryNew().copy(authorID = targetUser.id)
                val someOneElseArticle = articleService.createArticle(newArticle)
                articleService.publishArticle(someOneElseArticle.id)

                val update = articleEntryUpdate().copy(id = someOneElseArticle.id)
                Assertions.assertThrows(NotAuthorized::class.java) {
                    articleClient.deleteArticle(someOneElseArticle.id)
                }
                Assertions.assertThrows(NotAuthorized::class.java) {
                    articleClient.updateArticle(update)
                }
                Assertions.assertThrows(NotAuthorized::class.java) {
                    articleClient.publishArticle(someOneElseArticle.id)
                }
                Assertions.assertThrows(NotAuthorized::class.java) {
                    articleClient.getArticleDraftByID(someOneElseArticle.id)
                }
                Assertions.assertThrows(NotAuthorized::class.java) {
                    articleClient.getArticleDraftHeaders(targetUser.id)
                }
            }
        }.toList()
    }

    @Test
    fun anyoneCanReadAnyonesPublishedArticles() {
        testDataManager.clearData()

        val targetUser = testDataManager.forceCreateUser(
                newUser().copy(email = "another@email.com", handle = "another")
        )

        val otherNewArticle = articleEntryNew().copy(authorID = targetUser.id)
        val otherArticle = articleService.createArticle(otherNewArticle)
        articleService.publishArticle(otherArticle.id)


        articleClient.getArticleById(otherArticle.id)
        articleClient.getArticleHeaders()
        articleClient.getArticleHeadersByAuthor(targetUser.id)

    }

}