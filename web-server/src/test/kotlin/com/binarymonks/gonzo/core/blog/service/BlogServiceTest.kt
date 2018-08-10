package com.binarymonks.gonzo.core.blog.service

import com.binarymonks.gonzo.blogEntryNew
import com.binarymonks.gonzo.core.blog.api.BlogEntry
import com.binarymonks.gonzo.core.blog.api.BlogEntryDraft
import com.binarymonks.gonzo.core.blog.api.BlogDraftEntryNew
import com.binarymonks.gonzo.core.common.NotFound
import com.binarymonks.gonzo.core.test.GonzoTestConfig
import com.binarymonks.gonzo.core.test.harness.TestDataManager
import com.binarymonks.gonzo.core.time.clock
import com.binarymonks.gonzo.core.users.api.User
import com.binarymonks.gonzo.core.users.service.UserService
import com.binarymonks.gonzo.newUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.support.AnnotationConfigContextLoader
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@ExtendWith(SpringExtension::class)
@ContextConfiguration(
        classes = [
            GonzoTestConfig::class
        ],
        loader = AnnotationConfigContextLoader::class
)
class BlogServiceTest {

    lateinit var mockClock: Clock

    @Autowired
    lateinit var blogService: BlogService
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var testDataManager: TestDataManager

    lateinit var user: User

    @BeforeEach
    fun setUp() {
        testDataManager.clearData()
        mockClock = Mockito.mock(Clock::class.java)
        clock = mockClock
        user = userService.createUser(newUser())
        itIsNow()
    }

    @Test
    fun createAndGetBlogEntryDraft() {
        val now = itIsNow()

        val newBlogEntry = BlogDraftEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val created = blogService.createBlogEntry(newBlogEntry)

        val expectedBlogEntry = BlogEntryDraft(
                id = created.id,
                title = newBlogEntry.title,
                content = newBlogEntry.content,
                author = user.toPublicHeader(),
                published = false,
                unpublishedChanges = true,
                updated = now,
                created = now
        )

        Assertions.assertEquals(expectedBlogEntry, created)

        val retrieved = blogService.getBlogEntryDraftByID(created.id)

        Assertions.assertEquals(expectedBlogEntry, retrieved)
    }

    @Test
    fun createBlogEntry_GetBlogEntryThatHasNotBeenPublished() {
        val newBlogEntry = BlogDraftEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val created = blogService.createBlogEntry(newBlogEntry)

        Assertions.assertThrows(NotFound::class.java) {
            blogService.getBlogEntryById(created.id)
        }
    }

    @Test
    fun updateBlogEntry_Unpublished() {
        val now = itIsNow()

        val newBlogEntry = BlogDraftEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val createdDraft = blogService.createBlogEntry(newBlogEntry)

        val later = itIsNow(now.plusDays(1))

        val update = createdDraft.toUpdate().copy(
                title = "changed${createdDraft.title}",
                content = "changed${createdDraft.content}"
        )

        val expected = createdDraft.copy(
                title = update.title,
                content = update.content,
                updated = later
        )

        val updated = blogService.updateBlogEntry(update)

        Assertions.assertEquals(expected, updated)
        Assertions.assertEquals(expected, blogService.getBlogEntryDraftByID(createdDraft.id))
    }

    @Test
    fun publishBlogEntry() {
        val createdTime = itIsNow()

        val newBlogEntry = BlogDraftEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val createdDraft = blogService.createBlogEntry(newBlogEntry)

        val publishTime = itIsNow(createdTime.plusDays(1))

        blogService.publishBlogEntry(createdDraft.id)

        val expectedDraft = createdDraft.copy(
                published = true,
                unpublishedChanges = false
        )
        val expectedBlogEntry = BlogEntry(
                id = createdDraft.id,
                title = createdDraft.title,
                content = createdDraft.content,
                author = createdDraft.author,
                publishedOn = publishTime,
                lastEdited = publishTime
        )

        Assertions.assertEquals(expectedDraft, blogService.getBlogEntryDraftByID(createdDraft.id))
        Assertions.assertEquals(expectedBlogEntry, blogService.getBlogEntryById(createdDraft.id))

    }

    @Test
    fun updateBlogEntry_realChanges_AlreadyPublished() {
        val createdTime = itIsNow()

        val newBlogEntry = BlogDraftEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val createdDraft = blogService.createBlogEntry(newBlogEntry)

        val publishTime = itIsNow(createdTime.plusDays(1))

        blogService.publishBlogEntry(createdDraft.id)

        val updateTime = itIsNow(publishTime.plusDays(1))

        val update = createdDraft.toUpdate().copy(
                title = "changed${createdDraft.title}",
                content = "changed${createdDraft.content}"
        )

        val expectedBlogDraft = createdDraft.copy(
                title = update.title,
                content = update.content,
                updated = updateTime,
                published = true,
                unpublishedChanges = true
        )
        val expectedBlogEntry = BlogEntry(
                id = createdDraft.id,
                title = createdDraft.title,
                content = createdDraft.content,
                author = createdDraft.author,
                publishedOn = publishTime,
                lastEdited = publishTime
        )

        val updated = blogService.updateBlogEntry(update)

        Assertions.assertEquals(expectedBlogDraft, updated)
        Assertions.assertEquals(expectedBlogEntry, blogService.getBlogEntryById(createdDraft.id))

    }

    @Test
    fun updateBlogEntry_noChange_AlreadyPublished() {
        val createdTime = itIsNow()

        val newBlogEntry = BlogDraftEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val createdDraft = blogService.createBlogEntry(newBlogEntry)

        val publishTime = itIsNow(createdTime.plusDays(1))

        blogService.publishBlogEntry(createdDraft.id)

        itIsNow(publishTime.plusDays(1))

        val update = createdDraft.toUpdate().copy()

        val expectedBlogDraft = createdDraft.copy(
                title = update.title,
                content = update.content,
                published = true,
                unpublishedChanges = false
        )
        val expectedBlogEntry = BlogEntry(
                id = createdDraft.id,
                title = createdDraft.title,
                content = createdDraft.content,
                author = createdDraft.author,
                publishedOn = publishTime,
                lastEdited = publishTime
        )

        val updated = blogService.updateBlogEntry(update)

        Assertions.assertEquals(expectedBlogDraft, updated)
        Assertions.assertEquals(expectedBlogEntry, blogService.getBlogEntryById(createdDraft.id))

    }

    @Test
    fun publishBlogEntry_withChanges_AlreadyPublished() {
        val createdTime = itIsNow()

        val newBlogEntry = BlogDraftEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val createdDraft = blogService.createBlogEntry(newBlogEntry)

        val publishTime = itIsNow(createdTime.plusDays(1))

        blogService.publishBlogEntry(createdDraft.id)

        val updateTime = itIsNow(publishTime.plusDays(1))

        val update = createdDraft.toUpdate().copy(
                title = "changed${createdDraft.title}",
                content = "changed${createdDraft.content}"
        )

        val updated = blogService.updateBlogEntry(update)

        val publishedAgainTime = itIsNow(updateTime.plusDays(1))

        blogService.publishBlogEntry(createdDraft.id)

        val expectedBlogDraft = createdDraft.copy(
                title = update.title,
                content = update.content,
                updated = updateTime,
                published = true,
                unpublishedChanges = false
        )
        val expectedBlogEntry = BlogEntry(
                id = createdDraft.id,
                title = updated.title,
                content = updated.content,
                author = createdDraft.author,
                publishedOn = publishTime,
                lastEdited = publishedAgainTime
        )

        Assertions.assertEquals(expectedBlogDraft, blogService.getBlogEntryDraftByID(createdDraft.id))
        Assertions.assertEquals(expectedBlogEntry, blogService.getBlogEntryById(createdDraft.id))
    }


    @Test
    fun getBlogEntryHeaders_allUsers_onlyPublished() {
        blogService.createBlogEntry(blogEntryNew().copy(
                title = "Entry1",
                authorID = user.id
        )).toHeader() // Not published, should not see this one

        val blogEntryUser1_Published = blogService.createBlogEntry(blogEntryNew().copy(
                title = "Entry2",
                authorID = user.id
        )).toHeader()
        blogService.publishBlogEntry(blogEntryUser1_Published.id)

        val user2 = userService.createUser(newUser().copy("another@blah.com", "another"))
        val blogEntryUser2_Published = blogService.createBlogEntry(blogEntryNew().copy(
                title = "Entry3",
                authorID = user2.id
        )).toHeader()
        blogService.publishBlogEntry(blogEntryUser2_Published.id)

        val expectedHeaders = listOf(blogEntryUser1_Published, blogEntryUser2_Published)


        val actual = blogService.getBlogEntryHeaders()
        Assertions.assertEquals(actual.size, expectedHeaders.size)
        for (header in expectedHeaders) {
            Assertions.assertTrue(actual.contains(header))
        }
    }

    @Test
    fun getBlogEntryHeadersByAuthor_onlyAuthor_onlyPublished() {
        blogService.createBlogEntry(blogEntryNew().copy(
                title = "Entry1",
                authorID = user.id
        )).toHeader() // Not published, should not see this one

        val draftUser1Published = blogService.createBlogEntry(blogEntryNew().copy(
                title = "User1Published",
                authorID = user.id
        )).toHeader()
        blogService.publishBlogEntry(draftUser1Published.id)
        val user1PublishedHeader = blogService.getBlogEntryById(draftUser1Published.id).toHeader()

        val user2 = userService.createUser(newUser().copy("another@blah.com", "another"))
        val draftUser2Created = blogService.createBlogEntry(blogEntryNew().copy(
                title = "User2Published",
                authorID = user2.id
        )) // Not right author, should not see this one
        blogService.publishBlogEntry(draftUser2Created.id)

        val expectedHeaders = listOf(user1PublishedHeader)

        val actual = blogService.getBlogEntryHeadersByAuthor(user.id)
        Assertions.assertEquals(actual.size, expectedHeaders.size)
        for (header in expectedHeaders) {
            Assertions.assertTrue(actual.contains(header))
        }
    }

    @Test
    fun getBlogEntryDraftHeaders_allAuthorsBlogs() {
        val blogEntryUser1_UnPublished = blogService.createBlogEntry(blogEntryNew().copy(
                title = "Entry1",
                authorID = user.id
        )).toHeader()

        val blogEntryUser1_Published = blogService.createBlogEntry(blogEntryNew().copy(
                title = "Entry2",
                authorID = user.id
        )).toHeader()
        blogService.publishBlogEntry(blogEntryUser1_Published.id)

        val user2 = userService.createUser(newUser().copy("another@blah.com", "another"))
        val blogEntryUser2_Published = blogService.createBlogEntry(blogEntryNew().copy(
                title = "Entry3",
                authorID = user2.id
        )).toHeader() // Not right author, should not see this one
        blogService.publishBlogEntry(blogEntryUser2_Published.id)

        val expectedHeaders = listOf(blogEntryUser1_Published, blogEntryUser1_UnPublished)


        val actual = blogService.getBlogEntryDraftHeaders(user.id)
        Assertions.assertEquals(actual.size, expectedHeaders.size)
        for (header in expectedHeaders) {
            Assertions.assertTrue(actual.contains(header))
        }
    }

    @Test
    fun deleteBlogEntry_alreadyPublished(){
        val newBlogEntry = BlogDraftEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )
        val createdDraft = blogService.createBlogEntry(newBlogEntry)
        blogService.publishBlogEntry(createdDraft.id)

        blogService.deleteBlogEntry(createdDraft.id)

        Assertions.assertThrows(NoSuchElementException::class.java) {
            blogService.getBlogEntryDraftByID(createdDraft.id)
        }
    }


    /**
     * Helper for setting the mock time.
     */
    private fun itIsNow(now: ZonedDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC"))): ZonedDateTime {
        Mockito.`when`(mockClock.instant()).thenReturn(now.toInstant())
        return now
    }

}