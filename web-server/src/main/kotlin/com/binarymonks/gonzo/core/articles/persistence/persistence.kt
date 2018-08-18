package com.binarymonks.gonzo.core.articles.persistence

import com.binarymonks.gonzo.core.articles.api.Article
import com.binarymonks.gonzo.core.articles.api.ArticleDraft
import com.binarymonks.gonzo.core.common.NotFound
import com.binarymonks.gonzo.core.extensions.time.normalise
import com.binarymonks.gonzo.core.users.persistence.UserEntity
import org.springframework.data.repository.CrudRepository
import java.time.ZonedDateTime
import javax.persistence.*


@Entity
data class ArticlePublished(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = -1,

        @Column(nullable = false)
        var title: String = "",

        @Lob
        @Column(nullable = false)
        var content: String = "",

        @Column(nullable = true)
        var created: ZonedDateTime? = null,

        @Column(nullable = false)
        var updated: ZonedDateTime? = null

)

@Entity
data class ArticleDraftEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(nullable = false)
        var title: String = "",

        @Lob
        @Column(nullable = false)
        var content: String = "",

        @ManyToOne(fetch = FetchType.EAGER)
        var author: UserEntity? = null,

        @Column(nullable = true)
        var created: ZonedDateTime? = null,

        @Column(nullable = false)
        var updated: ZonedDateTime? = null,

        @Column(nullable = true)
        var deleted: ZonedDateTime? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name="ArticleEntryPublishedID")
        var publishedArticle: ArticlePublished? = null,

        @Column(nullable = false)
        var unpublishedChanges: Boolean = true

        ) {
    fun toArticleEntryDraft() = ArticleDraft(
            id = id!!,
            title = title,
            content = content,
            author = author!!.toUser().toPublicHeader(),
            published = publishedArticle != null,
            unpublishedChanges = unpublishedChanges,
            created = checkNotNull(created).normalise(),
            updated = updated!!.normalise()
    )

    fun toArticleEntry(): Article {
        if (publishedArticle == null) {
            throw NotFound()
        }
        return Article(
                id = id!!,
                title = publishedArticle!!.title,
                content = publishedArticle!!.content,
                author = author!!.toUser().toPublicHeader(),
                lastEdited = publishedArticle!!.updated!!.normalise(),
                publishedOn = publishedArticle!!.created!!.normalise()
        )
    }
}

interface ArticleRepo : CrudRepository<ArticleDraftEntity, Long>{
    fun findAllByAuthor(user: UserEntity): Iterable<ArticleDraftEntity>
}