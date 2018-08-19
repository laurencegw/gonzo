import {Article, ArticleDraft, ArticleDraftNew, ArticleDraftUpdate, ArticleHeader, Articles} from "@/articles/api"
import {UserPublicHeader} from "@/users/api"
import axios from "axios"
import {getToken} from "@/common/token"


export class ArticlesClient implements Articles {
    private articlesBasePath = "api/articles"
    private usersBasePath = "api/users"

    private headers(token: string | String): any {
        return {Authorization: `Bearer ${token}`}
    }

    createArticleDraft(articleDraftNew: ArticleDraftNew): Promise<ArticleDraft> {
        return getToken().then((token) => {
            return axios.post<string>(this.articlesBasePath, articleDraftNew, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
                return new ArticleDraft(response.data)
            }
        )
    }

    deleteArticle(articleID: number): Promise<any> {
        return getToken().then((token) => {
            return axios.delete(`${this.articlesBasePath}/${articleID}`, {
                headers: this.headers(token),
                responseType: "json"
            })
        })
    }

    publishArticle(articleID: number): Promise<any> {
        return getToken().then((token) => {
            return axios.put<string>(
                `${this.articlesBasePath}/${articleID}/publish`,
                null,
                {
                    headers: this.headers(token),
                    responseType: "json"
                })
        })
    }

    getArticleDraftByID(articleID: number): Promise<ArticleDraft> {
        return getToken().then((token) => {
            return axios.get<ArticleDraft>(`${this.articlesBasePath}/${articleID}/draft`, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
            return new ArticleDraft(response.data)
        })
    }

    getArticleByID(articleID: number): Promise<Article> {
        return axios.get<Article>(`${this.articlesBasePath}/${articleID}`, {
            responseType: "json"
        }).then((response) => {
            return new Article(response.data)
        })
    }

    getArticleDraftHeaders(authorID: number): Promise<Array<ArticleHeader>> {
        return getToken().then((token) => {
            return axios.get<Array<any>>(`${this.usersBasePath}/${authorID}/drafts`, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
            const articleHeaders: Array<any> = response.data
            return articleHeaders.map((articleHeaderObj) => new ArticleHeader(articleHeaderObj))
        })
    }

    updateArticleDraft(articleDraftUpdate: ArticleDraftUpdate): Promise<ArticleDraft> {
        return getToken().then((token) => {
            return axios.put<string>(`${this.articlesBasePath}/${articleDraftUpdate.id}/`, articleDraftUpdate, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
                return new ArticleDraft(response.data)
            }
        )
    }

    getAllArticleHeaders(): Promise<Array<ArticleHeader>> {
        return axios.get<Array<any>>(this.articlesBasePath, {
            responseType: "json"
        }).then((response) => {
            const articleHeaders: Array<any> = response.data
            return articleHeaders.map((articleHeaderObj) => new ArticleHeader(articleHeaderObj))
        })
    }
}

export class ArticlesClientFake implements Articles {

    createArticleDraft(articleDraftNew: ArticleDraftNew): Promise<ArticleDraft> {
        return Promise.resolve(new ArticleDraft({
            id: 1,
            title: articleDraftNew.title,
            content: articleDraftNew.content,
            author: new UserPublicHeader(
                2,
                "user1",
                "Sarah",
                "Smiith"
            ),
            published: false,
            unpublishedChanges: false,
            created: new Date(),
            updated: new Date()
        }))
    }

    deleteArticle(articleID: number): Promise<any> {
        throw Error("Not Implemented")
    }

    publishArticle(articleID: number): Promise<any> {
        throw Error("Not Implemented")
    }

    getArticleDraftByID(articleID: number): Promise<ArticleDraft> {
        throw Error("Not Implemented")
    }

    getArticleDraftHeaders(authorID: number): Promise<Array<ArticleHeader>> {
        throw Error("Not Implemented")
    }

    updateArticleDraft(articleDraftUpdate: ArticleDraftUpdate): Promise<ArticleDraft> {
        throw Error("Not Implemented")
    }

    getArticleByID(articleID: number): Promise<Article> {
        throw Error("Not Implemented")
    }

    getAllArticleHeaders(): Promise<Array<ArticleHeader>> {
        throw Error("Not Implemented")
    }
}

