import {BlogDraft, BlogDraftNew, BlogDraftUpdate, BlogHeader, Blogs} from "@/blogs/api"
import {UserPublicHeader} from "@/users/api"
import axios from "axios"
import {getToken} from "@/common/token"


export class BlogsClient implements Blogs {
    private blogsBasePath = "api/blogs"
    private usersBasePath = "api/users"
    private

    private headers(token: string): any {
        return {Authorization: `Bearer ${token}`}
    }

    createBlogDraft(blogDraftNew: BlogDraftNew): Promise<BlogDraft> {
        return getToken().then((token) => {
            return axios.post<string>(this.blogsBasePath, blogDraftNew, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
                return new BlogDraft(response.data)
            }
        )
    }

    getBlogDraftByID(blogID: number): Promise<BlogDraft> {
        const token = localStorage.getItem(this.tokenKey)
        if (token === null) {
            return Promise.reject(new Error("Not logged in"))
        }
        return new Promise<BlogDraft>((resolve, reject) => {
            axios.get<BlogDraft>(`${this.blogsBasePath}/${blogID}/draft`, {
                headers: this.headers(token),
                responseType: "json"
            }).then((response) => {
                    resolve(new BlogDraft(response.data))
                }
            ).catch((rejection) => {
                reject(rejection)
            })
        })
    }

    getBlogDraftHeaders(authorID: number): Promise<Array<BlogHeader>> {
        const token = localStorage.getItem(this.tokenKey)
        if (token === null) {
            return Promise.reject(new Error("Not logged in"))
        }
        return new Promise<Array<BlogHeader>>((resolve, reject) => {
            axios.get<Array<any>>(`${this.usersBasePath}/${authorID}/drafts`, {
                headers: this.headers(token),
                responseType: "json"
            }).then((response) => {
                    const blogHeaders: Array<any> = response.data
                    resolve(blogHeaders.map((blogHeaderObj) => new BlogHeader(blogHeaderObj)))
                }
            ).catch((rejection) => {
                reject(rejection)
            })
        })
    }

    updateBlogDraft(blogDraftUpdate: BlogDraftUpdate): Promise<BlogDraft> {
        throw Error("Not Implemented")
    }
}

export class BlogsClientFake implements Blogs {

    createBlogDraft(blogDraftNew: BlogDraftNew): Promise<BlogDraft> {
        return Promise.resolve(new BlogDraft({
            id: 1,
            title: blogDraftNew.title,
            content: blogDraftNew.content,
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

    getBlogDraftByID(blogID: number): Promise<BlogDraft> {
        throw Error("Not Implemented")
    }

    getBlogDraftHeaders(authorID: number): Promise<Array<BlogHeader>> {
        throw Error("Not Implemented")
    }

    updateBlogDraft(blogDraftUpdate: BlogDraftUpdate): Promise<BlogDraft> {
        throw Error("Not Implemented")
    }
}

