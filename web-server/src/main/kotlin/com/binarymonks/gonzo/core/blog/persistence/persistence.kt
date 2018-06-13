package com.binarymonks.gonzo.core.blog.persistence

import com.binarymonks.gonzo.core.blog.api.BlogEntry
import com.binarymonks.gonzo.core.blog.api.BlogEntryDraft
import com.binarymonks.gonzo.core.common.NotFound
import com.binarymonks.gonzo.core.extensions.time.normalise
import com.binarymonks.gonzo.core.users.persistence.UserEntity
import org.springframework.data.repository.CrudRepository
import java.time.ZonedDateTime
import javax.persistence.*


@Entity
data class BlogEntryPublished(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var blogEntryDraftID: Long = -1,

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
data class BlogEntryDraftEntity(
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

        @OneToOne(cascade = [CascadeType.ALL])
        var publishedBlog: BlogEntryPublished? = null,

        @Column(nullable = false)
        var unpublishedChanges: Boolean = true

        ) {
    fun toBlogEntryDraft() = BlogEntryDraft(
            id = id!!,
            title = title,
            content = content,
            author = author!!.toUser().toPublicHeader(),
            published = publishedBlog != null,
            unpublishedChanges = unpublishedChanges,
            created = checkNotNull(created).normalise(),
            updated = updated!!.normalise()
    )

    fun toBlogEntry(): BlogEntry {
        if (publishedBlog == null) {
            throw NotFound()
        }
        return BlogEntry(
                id = id!!,
                title = publishedBlog!!.title,
                content = publishedBlog!!.content,
                author = author!!.toUser().toPublicHeader(),
                lastEdited = publishedBlog!!.updated!!.normalise(),
                publishedOn = publishedBlog!!.created!!.normalise()
        )
    }

    fun toBlogEntryPublished(): BlogEntryPublished {
        return BlogEntryPublished(
                title = title,
                content=content
        )
    }
}

interface BlogRepo : CrudRepository<BlogEntryDraftEntity, Long>{
    fun findAllByAuthor(user: UserEntity): Iterable<BlogEntryDraftEntity>
}