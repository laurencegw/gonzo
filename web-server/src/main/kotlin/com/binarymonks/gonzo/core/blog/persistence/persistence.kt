package com.binarymonks.gonzo.core.blog.persistence

import com.binarymonks.gonzo.core.blog.api.BlogEntry
import com.binarymonks.gonzo.core.extensions.time.normalise
import com.binarymonks.gonzo.core.users.persistence.UserEntity
import org.springframework.data.repository.CrudRepository
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
data class BlogEntryEntity(
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

        @Column(nullable = false)
        var published: Boolean = false,

        @Column(nullable = true)
        var firstPublished: ZonedDateTime? = null,

        @Column(nullable = false)
        var created: ZonedDateTime? = null,

        @Column(nullable = false)
        var updated: ZonedDateTime? = null
) {
    fun toBlogEntry(): BlogEntry = BlogEntry(
            id = id!!,
            title = title,
            content = content,
            author = author!!.toUser().toPublicHeader(),
            published = published,
            publishedOn = firstPublished?.normalise(),
            created = created!!.normalise(),
            updated = updated!!.normalise()
    )
}

interface BlogRepo : CrudRepository<BlogEntryEntity, Long>