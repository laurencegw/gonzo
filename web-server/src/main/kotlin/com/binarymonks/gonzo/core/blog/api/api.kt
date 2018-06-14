package com.binarymonks.gonzo.core.blog.api

import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.common.Resource
import com.binarymonks.gonzo.core.common.Types
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
    fun getBlogEntryDraftHeaders(authorID: Long): List<BlogEntryHeader>
}

interface BlogAuth {
    fun createBlogEntry(credentials: Credentials, blogEntryNew: BlogEntryNew): BlogEntryDraft

    fun updateBlogEntry(credentials: Credentials,update: BlogEntryUpdate): BlogEntryDraft

    fun getBlogEntryDraftByID(credentials: Credentials,blogID: Long): BlogEntryDraft

    fun publishBlogEntry(credentials: Credentials,blogID: Long)

    fun getBlogEntryById(credentials: Credentials,id: Long): BlogEntry

    fun getBlogEntryHeaders(credentials: Credentials): List<BlogEntryHeader>

    fun getBlogEntryHeadersByAuthor(credentials: Credentials, authorID: Long): List<BlogEntryHeader>

    fun getBlogEntryDraftHeaders(credentials: Credentials,authorID: Long): List<BlogEntryHeader>
}

open class BlogResource: Resource(type = Types.BLOG){
    override fun attributes(): Map<String, Any?> {
        val atts = super.attributes().toMutableMap()
        if(this.hasProperty("author")){
            atts["authorID"]=(atts["author"] as UserPublicHeader).id
        }
        return atts
    }
}

data class BlogEntryNew @JsonCreator constructor(
        val title: String,
        val content: String,
        val authorID: Long
) : BlogResource()

data class BlogEntryUpdate @JsonCreator constructor(
        val id: Long,
        val title: String,
        val content: String
): BlogResource()

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
):BlogResource() {

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
