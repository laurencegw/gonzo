import {UserPublicHeader} from "@/users/api"

class BlogDraftEntryNew {
    title: string
    content: string
    authorID: number

    constructor(title: string, content: string, authorID: number) {
        this.title = title
        this.content = content
        this.authorID = authorID
    }
}

class BlogDraftEntryUpdate {
    id: number
    title: string
    content: string
    constructor(id: number, title: string, content: string) {
        this.id = id
        this.title = title
        this.content = content
    }
}

/**
 * Publicly viewable info for a  published blog
 */
class BlogEntry {
    id: number
    title: string
    content: string
    author: UserPublicHeader
    lastEdited: Date
    publishedOn: Date

    constructor(id: number, title: string, content: string, author: UserPublicHeader, lastEdited: Date, publishedOn: Date) {
        this.id = id
        this.title = title
        this.content = content
        this.author = author
        this.lastEdited = lastEdited
        this.publishedOn = publishedOn
    }
}

/**
 * Representation of a blog for the Author.
 */
class BlogEntryDraft {
    id: number
    title: string
    content: string
    author: UserPublicHeader
    published: Boolean
    unpublishedChanges: Boolean
    created: Date
    updated: Date

    constructor(id: number, title: string, content: string, author: UserPublicHeader, published: Boolean,
                unpublishedChanges: Boolean, created: Date, updated: Date) {
        this.id = id
        this.title = title
        this.content = content
        this.author = author
        this.published = published
        this.unpublishedChanges = unpublishedChanges
        this.created = created
        this.updated = updated
    }
}



class BlogEntryHeader {
    id: number
    title: string
    author: UserPublicHeader
    updated: Date
    created: Date


    constructor(id: number, title: string, author: UserPublicHeader, updated: Date, created: Date) {
        this.id = id
        this.title = title
        this.author = author
        this.updated = updated
        this.created = created
    }
}

