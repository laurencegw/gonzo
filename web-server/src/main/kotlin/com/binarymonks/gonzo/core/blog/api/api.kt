package com.binarymonks.gonzo.core.blog.api

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime


interface Blog {
    fun createBlogEntry(blogEntryNew: BlogEntryNew): BlogEntry
    fun updateBlogEntry(update: BlogEntryUpdate): BlogEntry
    fun getBlogEntryHeaders(): List<BlogEntryHeader>
    fun getBlogEntryById(id: Long): BlogEntry
}

data class BlogEntryNew @JsonCreator constructor(
        val title: String,
        val content: String,
        val published: Boolean
)

data class BlogEntryUpdate @JsonCreator constructor(
        val id: Long = -1,
        val title: String,
        val content: String,
        val published: Boolean
)

data class BlogEntry @JsonCreator constructor(
        var id: Long,
        var title: String,
        var content: String,
        var published: Boolean,
        var created: ZonedDateTime,
        var updated: ZonedDateTime,
        var publishedOn: ZonedDateTime?
) {

    fun toUpdate(): BlogEntryUpdate = BlogEntryUpdate(
            id = id,
            title = title,
            content = content,
            published = published
    )

    fun toHeader(): BlogEntryHeader = BlogEntryHeader(
            id = id,
            title = title,
            published = published,
            updated = updated,
            created = created,
            publishedOn = publishedOn
    )
}

data class BlogEntryHeader @JsonCreator constructor(
        val id: Long,
        val title: String,
        val published: Boolean,
        val created: ZonedDateTime,
        val updated: ZonedDateTime,
        val publishedOn: ZonedDateTime?
)
