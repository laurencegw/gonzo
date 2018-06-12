package com.binarymonks.gonzo.core.blog.api

import com.binarymonks.gonzo.core.users.api.UserPublicHeader
import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime

interface Blog {
    fun createBlogEntry(blogEntryNew: BlogEntryNew): BlogEntryDraft
    fun updateBlogEntry(update: BlogEntryUpdate): BlogEntryDraft
    fun getBlogEntryDraftByID(blogID: Long): BlogEntryDraft
    fun publishBlogEntry(blogID: Long)
    fun getBlogEntryById(id: Long): BlogEntry
    fun getBlogEntryHeaders(): List<BlogEntryHeader>
    fun getBlogEntryDraftHeaders(publisherID:Long): List<BlogEntryHeader>
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
 * The initially created draft blog
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

/**
 * Publicly viewable header for a published blog.
 */
data class BlogEntryHeader @JsonCreator constructor(
        val id: Long,
        val title: String,
        val author: UserPublicHeader,
        val updated: ZonedDateTime,
        val created: ZonedDateTime
)
