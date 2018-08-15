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

export class BlogHeader {
    id: number
    title: string
    author: UserPublicHeader
    updated: Date
    created: Date


    constructor(obj: any) {
        this.id = obj.id
        this.title = obj.title
        this.author = obj.author
        this.updated = obj.updated
        this.created = obj.created
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

    constructor(obj: any) {
        this.id = obj.id
        this.title = obj.title
        this.content = obj.content
        this.author = obj.author
        this.lastEdited = obj.lastEdited
        this.publishedOn = obj.publishedOn
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

    constructor(obj: any) {
        this.id = obj.id
        this.title = obj.title
        this.content = obj.content
        this.author = obj.author
        this.published = obj.published
        this.unpublishedChanges = obj.unpublishedChanges
        this.created = obj.created
        this.updated = obj.updated
    }

    toHeader(): BlogHeader {
        return new BlogHeader(this)
    }
}


export interface Blogs {
    createBlogDraft(blogDraftNew: BlogDraftNew): Promise<BlogDraft>

    deleteBlog(blogID: number): Promise<null>

    publishBlog(blogID: number): Promise<null>

    updateBlogDraft(blogDraftUpdate: BlogDraftUpdate): Promise<BlogDraft>

    getBlogDraftHeaders(authorID: number): Promise<Array<BlogHeader>>

    getBlogDraftByID(blogID: number): Promise<BlogDraft>

    getBlogByID(blogID: number): Promise<Blog>
}

