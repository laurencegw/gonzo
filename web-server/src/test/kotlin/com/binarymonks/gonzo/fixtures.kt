package com.binarymonks.gonzo

import com.binarymonks.gonzo.core.blog.api.BlogEntry
import com.binarymonks.gonzo.core.blog.api.BlogEntryHeader
import com.binarymonks.gonzo.core.blog.api.BlogEntryNew
import com.binarymonks.gonzo.core.blog.api.BlogEntryUpdate
import com.binarymonks.gonzo.core.extensions.time.normalise
import java.time.ZonedDateTime


fun blogEntryNew(): BlogEntryNew {
    return BlogEntryNew(
            title = "Some Blog Entry",
            content = "A bit of content",
            published = true
    )
}

fun blogEntry(): BlogEntry {
    return BlogEntry(
            id = 2,
            title = "Some Blog Entry",
            content = "A bit of content",
            published = true,
            created = ZonedDateTime.now().normalise(),
            updated = ZonedDateTime.now().normalise(),
            publishedOn = ZonedDateTime.now().normalise()
    )
}

fun blogEntryUpdate(): BlogEntryUpdate {
    return BlogEntryUpdate(
            id = 3,
            title = "Some Blog Entry Update",
            content = "A bit of content Updated",
            published = false
    )
}

fun blogEntryHeader():BlogEntryHeader {
    return BlogEntryHeader(
            id = 2,
            title = "Some Blog Entry",
            published = true,
            created = ZonedDateTime.now().normalise(),
            updated = ZonedDateTime.now().normalise(),
            publishedOn = ZonedDateTime.now().normalise()
    )
}