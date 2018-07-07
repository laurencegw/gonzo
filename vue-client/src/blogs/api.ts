import {UserPublicHeader} from "@/users/api"

export class BlogDraftNew {
    title: string
    content: string
    authorID: number

    constructor(title: string, content: string, authorID: number) {
        this.title = title
        this.content = content
        this.authorID = authorID
    }
}

export class BlogDraftUpdate {
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
export class Blog {
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
export class BlogDraft {
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

export class BlogHeader {
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

export interface Blogs {
    createBlogDraft(token: string, blogDraftNew: BlogDraftNew):  Promise<BlogDraft>
    updateBlogDraft(token: string, blogDraftUpdate: BlogDraftUpdate): Promise<BlogDraft>
    getBlogDraftHeaders(token: string, authorID: number): Promise<Array<BlogHeader>>
    getBlogDraftByID(token: string, blogID: number): Promise<BlogDraft>
}

