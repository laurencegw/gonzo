package com.binarymonks.me.core.blog.persistence

import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.repository.CrudRepository
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
data class BlogEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(nullable = false)
        var title: String,

        @Lob
        @Column(nullable = false)
        var content: String,

        @Column(nullable = false)
        var published: Boolean,

        @Column(nullable = true)
        var firstPublished: ZonedDateTime? = null,

        @CreatedDate
        var created: ZonedDateTime,

        @UpdateTimestamp
        var updated: ZonedDateTime
)

interface BlogRepo : CrudRepository<BlogEntry, Long>