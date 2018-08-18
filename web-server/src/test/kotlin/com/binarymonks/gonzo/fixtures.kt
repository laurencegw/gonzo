package com.binarymonks.gonzo

import com.binarymonks.gonzo.core.authz.api.AccessRequest
import com.binarymonks.gonzo.core.article.api.*
import com.binarymonks.gonzo.core.extensions.time.normalise
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.api.User
import com.binarymonks.gonzo.core.users.api.UserNew
import java.time.ZonedDateTime


fun articleEntryNew(): ArticleDraftEntryNew {
    return ArticleDraftEntryNew(
            title = "Some Article Entry",
            content = "A bit of content",
            authorID = 1
    )
}

fun articleEntryDraft() = ArticleEntryDraft(
        id = 2,
        title = "Some Article Entry",
        content = "A bit of content",
        published = false,
        unpublishedChanges = true,
        author = user().toPublicHeader(),
        updated = ZonedDateTime.now().normalise(),
        created = ZonedDateTime.now().normalise()
)

fun articleEntry(): ArticleEntry {
    return ArticleEntry(
            id = 2,
            title = "Some Article Entry",
            content = "A bit of content",
            author = user().toPublicHeader(),
            lastEdited = ZonedDateTime.now().normalise(),
            publishedOn = ZonedDateTime.now().normalise()
    )
}

fun articleEntryUpdate(): ArticleDraftEntryUpdate {
    return ArticleDraftEntryUpdate(
            id = 3,
            title = "Some Article Entry Update",
            content = "A bit of content Updated"
    )
}

fun articleEntryHeader(): ArticleEntryHeader {
    return ArticleEntryHeader(
            id = 2,
            title = "Some Article Entry",
            author = user().toPublicHeader(),
            created = ZonedDateTime.now().normalise(),
            updated = ZonedDateTime.now().normalise()
    )
}

fun newUser(): UserNew {
    return UserNew(
            email = "jane@somewhere.com",
            handle = "Jane",
            password = "password"
    )
}

fun user(): User {
    return User(
            id = -1,
            email = "jane@somewhere.com",
            handle = "Janey",
            firstName = "Jane",
            lastName = "Smith",
            role = Role.READER
    )
}

fun accessRequest(): AccessRequest{
    return AccessRequest(
            mapOf(),
            "",
            mapOf()
    )
}