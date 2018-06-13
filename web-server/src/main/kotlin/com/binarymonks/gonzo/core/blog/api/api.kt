package com.binarymonks.gonzo.core.blog.api

import com.binarymonks.gonzo.core.users.api.UserPublicHeader
import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime

interface Blog {

    /**
     * Creates a new blog that is not initially published.
     */
    fun createBlogEntry(blogEntryNew: BlogEntryNew): BlogEntryDraft

    /**
     * Updates the draft state of a blog. Changes will still need to be published.
     */
    fun updateBlogEntry(update: BlogEntryUpdate): BlogEntryDraft

    /**
     * Retrieves the draft state of a blog.
     */
    fun getBlogEntryDraftByID(blogID: Long): BlogEntryDraft

    /**
     * Publishes any unpublished changes in the draft state of the blog.
     */
    fun publishBlogEntry(blogID: Long)

    /**
     * Get the published state of a blog.
     */
    fun getBlogEntryById(id: Long): BlogEntry

    /**
     * Retrieves headers for published blogs
     */
    fun getBlogEntryHeaders(): List<BlogEntryHeader>

    /**
     * Retrieves headers for an authors published blogs
     *
     * @param authorID: [com.binarymonks.gonzo.core.users.api.User.id]
     */
    fun getBlogEntryHeadersByAuthor(authorID: Long): List<BlogEntryHeader>

    /**
     * Retrieves headers for all of an Authors blogs (published/unpublished).
     *
     * @param authorID: [com.binarymonks.gonzo.core.users.api.User.id]
     */
    fun getBlogEntryDraftHeaders(authorID:Long): List<BlogEntryHeader>
}

data class BlogEntryNew @JsonCreator constructor(
        val title: String,
        val content: String,
        val authorID: Long
)

data class BlogEntryUpdate @JsonCreator constructor(
        val id: Long,
        val title: String,
        val content: String
)

/**
 * Publicly viewable info for a  published blog
 */
data class BlogEntry @JsonCreator constructor(
        val id: Long,
        val title: String,
        val content: String,
        val author: UserPublicHeader,
        val lastEdited: ZonedDateTime,
        val publishedOn: ZonedDateTime
) {

    fun toUpdate(): BlogEntryUpdate = BlogEntryUpdate(
            id = id,
            title = title,
            content = content
    )

    fun toHeader(): BlogEntryHeader = BlogEntryHeader(
            id = id,
            title = title,
            author = author,
            updated = lastEdited,
            created = publishedOn
    )
}

/**
 * Representation of a blog for the Author.
 */
data class BlogEntryDraft(
        val id: Long,
        val title: String,
        val content: String,
        val author: UserPublicHeader,
        val published: Boolean,
        val unpublishedChanges: Boolean,
        val created: ZonedDateTime,
        val updated: ZonedDateTime
) {

    fun toUpdate(): BlogEntryUpdate = BlogEntryUpdate(
            id = id,
            title = title,
            content = content
    )


    fun toHeader(): BlogEntryHeader = BlogEntryHeader(
            id = id,
            title = title,
            author = author,
            updated = updated,
            created = created
    )
}


data class BlogEntryHeader @JsonCreator constructor(
        val id: Long,
        val title: String,
        val author: UserPublicHeader,
        val updated: ZonedDateTime,
        val created: ZonedDateTime
)
