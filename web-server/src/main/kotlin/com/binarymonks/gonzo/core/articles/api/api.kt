package com.binarymonks.gonzo.core.articles.api

import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.common.Resource
import com.binarymonks.gonzo.core.common.Types
import com.binarymonks.gonzo.core.users.api.UserPublicHeader
import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime
import javax.validation.constraints.NotBlank

interface Articles {

    /**
     * Creates a new articles that is not initially published.
     */
    fun createArticle(articleNew: ArticleDraftNew): ArticleDraft

    /**
     * Updates the draft state of a articles. Changes will still need to be published.
     */
    fun updateArticle(update: ArticleDraftUpdate): ArticleDraft

    /**
     * Permanently delete a articles (published or unpublished)
     */
    fun deleteArticle(articleID: Long)

    /**
     * Retrieves the draft state of a articles.
     */
    fun getArticleDraftByID(articleID: Long): ArticleDraft

    /**
     * Publishes any unpublished changes in the draft state of the articles.
     */
    fun publishArticle(articleID: Long)

    /**
     * Get the published state of a articles.
     */
    fun getArticleById(id: Long): Article

    /**
     * Retrieves headers for published articles
     */
    fun getArticleHeaders(): List<ArticleHeader>

    /**
     * Retrieves headers for an authors published articles
     *
     * @param authorID: [com.binarymonks.gonzo.core.users.api.User.id]
     */
    fun getArticleHeadersByAuthor(authorID: Long): List<ArticleHeader>

    /**
     * Retrieves headers for all of an Authors articles (published/unpublished).
     *
     * @param authorID: [com.binarymonks.gonzo.core.users.api.User.id]
     */
    fun getArticleDraftHeaders(authorID: Long): List<ArticleHeader>
}

interface ArticleAuth {
    fun createArticle(credentials: Credentials, articleNew: ArticleDraftNew): ArticleDraft

    fun updateArticle(credentials: Credentials, update: ArticleDraftUpdate): ArticleDraft

    fun deleteArticle(credentials: Credentials, articleID: Long)

    fun getArticleDraftByID(credentials: Credentials, articleID: Long): ArticleDraft

    fun publishArticle(credentials: Credentials, articleID: Long)

    fun getArticleById(credentials: Credentials, id: Long): Article

    fun getArticleHeaders(credentials: Credentials): List<ArticleHeader>

    fun getArticleHeadersByAuthor(credentials: Credentials, authorID: Long): List<ArticleHeader>

    fun getArticleDraftHeaders(credentials: Credentials, authorID: Long): List<ArticleHeader>
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


data class ArticleDraftNew @JsonCreator constructor(
        @field:NotBlank(message = "Title is required")
        val title: String,
        val content: String,
        val authorID: Long
) : ArticleDraftResource()

data class ArticleDraftUpdate @JsonCreator constructor(
        val id: Long,
        val title: String,
        val content: String
) : ArticleDraftResource()

/**
 * Publicly viewable info for a  published articles
 */
data class Article @JsonCreator constructor(
        val id: Long,
        val title: String,
        val content: String,
        val author: UserPublicHeader,
        val lastEdited: ZonedDateTime,
        val publishedOn: ZonedDateTime
):ArticleResource(){

    fun toHeader(): ArticleHeader = ArticleHeader(
            id = id,
            title = title,
            author = author,
            updated = lastEdited,
            created = publishedOn
    )
}

/**
 * Representation of a articles for the Author.
 */
data class ArticleDraft(
        val id: Long,
        val title: String,
        val content: String,
        val author: UserPublicHeader,
        val published: Boolean,
        val unpublishedChanges: Boolean,
        val created: ZonedDateTime,
        val updated: ZonedDateTime
) : ArticleDraftResource() {

    fun toUpdate(): ArticleDraftUpdate = ArticleDraftUpdate(
            id = id,
            title = title,
            content = content
    )


    fun toHeader(): ArticleHeader = ArticleHeader(
            id = id,
            title = title,
            author = author,
            updated = updated,
            created = created
    )
}


data class ArticleHeader @JsonCreator constructor(
        val id: Long,
        val title: String,
        val author: UserPublicHeader,
        val updated: ZonedDateTime,
        val created: ZonedDateTime
)
