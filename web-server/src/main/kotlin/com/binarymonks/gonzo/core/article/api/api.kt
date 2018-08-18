package com.binarymonks.gonzo.core.article.api

import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.common.Resource
import com.binarymonks.gonzo.core.common.Types
import com.binarymonks.gonzo.core.users.api.UserPublicHeader
import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime
import javax.validation.constraints.NotBlank

interface Article {

    /**
     * Creates a new article that is not initially published.
     */
    fun createArticleEntry(articleEntryNew: ArticleDraftEntryNew): ArticleEntryDraft

    /**
     * Updates the draft state of a article. Changes will still need to be published.
     */
    fun updateArticleEntry(update: ArticleDraftEntryUpdate): ArticleEntryDraft

    /**
     * Permanently delete a article (published or unpublished)
     */
    fun deleteArticleEntry(articleID: Long)

    /**
     * Retrieves the draft state of a article.
     */
    fun getArticleEntryDraftByID(articleID: Long): ArticleEntryDraft

    /**
     * Publishes any unpublished changes in the draft state of the article.
     */
    fun publishArticleEntry(articleID: Long)

    /**
     * Get the published state of a article.
     */
    fun getArticleEntryById(id: Long): ArticleEntry

    /**
     * Retrieves headers for published articles
     */
    fun getArticleEntryHeaders(): List<ArticleEntryHeader>

    /**
     * Retrieves headers for an authors published articles
     *
     * @param authorID: [com.binarymonks.gonzo.core.users.api.User.id]
     */
    fun getArticleEntryHeadersByAuthor(authorID: Long): List<ArticleEntryHeader>

    /**
     * Retrieves headers for all of an Authors articles (published/unpublished).
     *
     * @param authorID: [com.binarymonks.gonzo.core.users.api.User.id]
     */
    fun getArticleEntryDraftHeaders(authorID: Long): List<ArticleEntryHeader>
}

interface ArticleAuth {
    fun createArticleEntry(credentials: Credentials, articleEntryNew: ArticleDraftEntryNew): ArticleEntryDraft

    fun updateArticleEntry(credentials: Credentials, update: ArticleDraftEntryUpdate): ArticleEntryDraft

    fun deleteArticleEntry(credentials: Credentials, articleID: Long)

    fun getArticleEntryDraftByID(credentials: Credentials, articleID: Long): ArticleEntryDraft

    fun publishArticleEntry(credentials: Credentials, articleID: Long)

    fun getArticleEntryById(credentials: Credentials, id: Long): ArticleEntry

    fun getArticleEntryHeaders(credentials: Credentials): List<ArticleEntryHeader>

    fun getArticleEntryHeadersByAuthor(credentials: Credentials, authorID: Long): List<ArticleEntryHeader>

    fun getArticleEntryDraftHeaders(credentials: Credentials, authorID: Long): List<ArticleEntryHeader>
}

open class AuthoredResource(type: String) : Resource(type) {
    override fun attributes(): Map<String, Any?> {
        val atts = super.attributes().toMutableMap()
        if (this.hasProperty("author")) {
            atts["authorID"] = (atts["author"] as UserPublicHeader).id
        }
        return atts
    }
}

open class ArticleDraftResource : AuthoredResource(type = Types.ARTICLE_DRAFT)
open class ArticleResource : AuthoredResource(type = Types.ARTICLE)


data class ArticleDraftEntryNew @JsonCreator constructor(
        @field:NotBlank(message = "Title is required")
        val title: String,
        val content: String,
        val authorID: Long
) : ArticleDraftResource()

data class ArticleDraftEntryUpdate @JsonCreator constructor(
        val id: Long,
        val title: String,
        val content: String
) : ArticleDraftResource()

/**
 * Publicly viewable info for a  published article
 */
data class ArticleEntry @JsonCreator constructor(
        val id: Long,
        val title: String,
        val content: String,
        val author: UserPublicHeader,
        val lastEdited: ZonedDateTime,
        val publishedOn: ZonedDateTime
):ArticleResource(){

    fun toHeader(): ArticleEntryHeader = ArticleEntryHeader(
            id = id,
            title = title,
            author = author,
            updated = lastEdited,
            created = publishedOn
    )
}

/**
 * Representation of a article for the Author.
 */
data class ArticleEntryDraft(
        val id: Long,
        val title: String,
        val content: String,
        val author: UserPublicHeader,
        val published: Boolean,
        val unpublishedChanges: Boolean,
        val created: ZonedDateTime,
        val updated: ZonedDateTime
) : ArticleDraftResource() {

    fun toUpdate(): ArticleDraftEntryUpdate = ArticleDraftEntryUpdate(
            id = id,
            title = title,
            content = content
    )


    fun toHeader(): ArticleEntryHeader = ArticleEntryHeader(
            id = id,
            title = title,
            author = author,
            updated = updated,
            created = created
    )
}


data class ArticleEntryHeader @JsonCreator constructor(
        val id: Long,
        val title: String,
        val author: UserPublicHeader,
        val updated: ZonedDateTime,
        val created: ZonedDateTime
)
