import {UserPublicHeader} from "@/users/api"

export class ArticleDraftNew {
    title: string
    content: string
    authorID: number

    constructor(title: string, content: string, authorID: number) {
        this.title = title
        this.content = content
        this.authorID = authorID
    }
}

export class ArticleDraftUpdate {
    id: number
    title: string
    content: string

    constructor(id: number, title: string, content: string) {
        this.id = id
        this.title = title
        this.content = content
    }
}

export class ArticleHeader {
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
 * Publicly viewable info for a  published articles
 */
export class Article {
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
 * Representation of a articles for the Author.
 */
export class ArticleDraft {
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

    toHeader(): ArticleHeader {
        return new ArticleHeader(this)
    }
}


export interface Articles {
    createArticleDraft(articleDraftNew: ArticleDraftNew): Promise<ArticleDraft>

    deleteArticle(articleID: number): Promise<null>

    publishArticle(articleID: number): Promise<null>

    updateArticleDraft(articleDraftUpdate: ArticleDraftUpdate): Promise<ArticleDraft>

    getArticleDraftHeaders(authorID: number): Promise<Array<ArticleHeader>>

    getArticleDraftByID(articleID: number): Promise<ArticleDraft>

    getArticleByID(articleID: number): Promise<Article>

    getAllArticleHeaders(): Promise<Array<ArticleHeader>>
}

