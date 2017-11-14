package com.binarymonks.me.core.blog.persistence

import java.time.ZonedDateTime

data class BlogEntry(
        var id: Long? = null,
        var title: String,
        var content: String,
        var published: Boolean,
        var firstPublished: ZonedDateTime? = null,
        var created: ZonedDateTime,
        var updated: ZonedDateTime
)