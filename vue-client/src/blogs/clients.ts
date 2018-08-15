import {Blog, BlogDraft, BlogDraftNew, BlogDraftUpdate, BlogHeader, Blogs} from "@/blogs/api"
import {UserPublicHeader} from "@/users/api"
import axios from "axios"
import {getToken} from "@/common/token"


export class BlogsClient implements Blogs {
    private blogsBasePath = "api/blogs"
    private usersBasePath = "api/users"

    private headers(token: string | String): any {
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

    deleteBlog(blogID: number): Promise<any> {
        return getToken().then((token) => {
            return axios.delete(`${this.blogsBasePath}/${blogID}`, {
                headers: this.headers(token),
                responseType: "json"
            })
        })
    }

    getBlogDraftByID(blogID: number): Promise<BlogDraft> {
        return getToken().then((token) => {
            return axios.get<BlogDraft>(`${this.blogsBasePath}/${blogID}/draft`, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
            return new BlogDraft(response.data)
        })
    }

    getBlogByID(blogID: number): Promise<Blog> {
        return getToken().then((token) => {
            return axios.get<Blog>(`${this.blogsBasePath}/${blogID}`, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
            return new Blog(response.data)
        })
    }

    getBlogDraftHeaders(authorID: number): Promise<Array<BlogHeader>> {
        return getToken().then((token) => {
            return axios.get<Array<any>>(`${this.usersBasePath}/${authorID}/drafts`, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
            const blogHeaders: Array<any> = response.data
            return blogHeaders.map((blogHeaderObj) => new BlogHeader(blogHeaderObj))
        })
    }

    updateBlogDraft(blogDraftUpdate: BlogDraftUpdate): Promise<BlogDraft> {
        return getToken().then((token) => {
            return axios.put<string>(`${this.blogsBasePath}/${blogDraftUpdate.id}/`, blogDraftUpdate, {
                headers: this.headers(token),
                responseType: "json"
            })
        }).then((response) => {
                return new BlogDraft(response.data)
            }
        )
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

    deleteBlog(blogID: number): Promise<any> {
        throw Error("Not Implemented")
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

    getBlogByID(blogID: number): Promise<Blog> {
        throw Error("Not Implemented")
    }
}

