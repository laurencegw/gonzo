import {Blog, BlogDraft, BlogDraftNew, BlogHeader, Blogs} from "@/blogs/api"
import {ActionContext, ActionTree, GetterTree, MutationTree} from "vuex"
import {cloneDeep} from "lodash"
import {isDev} from "@/utils"
import {BlogsClient} from "@/blogs/clients"
import Vue from "vue"

class MyContentState {
    blogHeadersByID: { [id: number]: BlogHeader } = {}
    blogIDs: Array<number> = []
    publishedBlog?: Blog
    blogDraft?: BlogDraft
    modifiedBlogDraft?: BlogDraft
}

const getters: GetterTree<MyContentState, any> = {
    blogIDs(state: MyContentState): Array<number> {
        return cloneDeep(state.blogIDs)
    },
    blogHeader(state: MyContentState, blogID: number): BlogHeader {
        return cloneDeep(state.blogHeadersByID[blogID])
    },
    blogHeaders(state: MyContentState): Array<BlogHeader> {
        return cloneDeep(state.blogIDs.map(id => state.blogHeadersByID[id]))
    },
    blogDraft(state: MyContentState): BlogDraft | undefined {
        return cloneDeep(state.blogDraft)
    },
    modifiedBlogDraft(state: MyContentState): BlogDraft | undefined {
        return cloneDeep(state.modifiedBlogDraft)
    },
    publishedBlog(state: MyContentState): Blog | undefined {
        return cloneDeep(state.publishedBlog)
    }
}

const mutations: MutationTree<MyContentState> = {
    setBlogHeaders(state: MyContentState, blogHeaders: Array<BlogHeader>) {
        const IDs: Array<number> = []
        const headersByID: { [id: number]: BlogHeader } = {}
        for (const header of blogHeaders) {
            IDs.push(header.id)
            headersByID[header.id] = header
        }
        Vue.set(state, "blogIDs", IDs)
        Vue.set(state, "blogHeadersByID", headersByID)
    },
    refreshBlogHeader(state: MyContentState, blogHeader: BlogHeader) {
        const headersByID: { [id: number]: BlogHeader } = state.blogHeadersByID
        headersByID[blogHeader.id] = blogHeader
        Vue.set(state, "blogHeadersByID", headersByID)
    },
    workOnBlog(state: MyContentState, blog: BlogDraft) {
        Vue.set(state, "blogDraft", cloneDeep(blog))
        Vue.set(state, "modifiedBlogDraft", cloneDeep(blog))
    },
    setPublishedBlog(state: MyContentState, blog: Blog) {
        Vue.set(state, "publishedBlog", blog)
    },
    addBlogHeader(state: MyContentState, blogHeader: BlogHeader) {
        state.blogIDs.unshift(blogHeader.id)
        Vue.set(state, "blogIDs", state.blogIDs)
        state.blogHeadersByID[blogHeader.id] = blogHeader
        Vue.set(state, "blogHeadersByID", state.blogHeadersByID)
    },
    updateBlogDraftAttribute(state: MyContentState, payload: { attributeName: string, value: any }) {
        Vue.set(state.modifiedBlogDraft!, payload.attributeName, payload.value)
    },
    deleteBlog(state: MyContentState, blogID) {
        const currentIDs = state.blogIDs
        const index = currentIDs.indexOf(blogID)
        if (index !== -1) {
            currentIDs.splice(index, 1)
        }
        Vue.set(state, "blogIDs", currentIDs)
    }
}

const buildActions = function (blogClient: Blogs): ActionTree<MyContentState, any> {
    const actions: ActionTree<MyContentState, any> = {
        loadDraftHeadersForAuthor(store: ActionContext<MyContentState, any>, authorID: number) {
            return blogClient.getBlogDraftHeaders(authorID).then((headers) => {
                store.commit("setBlogHeaders", headers)
                return headers
            })
        },
        createBlog(store: ActionContext<MyContentState, any>, newBlog: BlogDraftNew) {
            return blogClient.createBlogDraft(newBlog).then((blogDraft) => {
                store.commit("addBlogHeader", blogDraft.toHeader())
                store.commit("workOnBlog", blogDraft)
                return blogDraft
            })
        },
        deleteBlog(store: ActionContext<MyContentState, any>) {
            const currentBlogID = store.getters.blogDraft.id
            return blogClient.deleteBlog(currentBlogID).then((blogDraft) => {
                store.commit("deleteBlog", currentBlogID)
                store.commit("workOnBlog", null)
            })
        },
        publishBlog(store: ActionContext<MyContentState, any>) {
            const currentBlogID = store.getters.blogDraft.id
            return blogClient.publishBlog(currentBlogID).then(() => {
                return store.dispatch("loadBlogDraft", currentBlogID).then((blogDraft: BlogDraft) => {
                    store.commit("refreshBlogHeader", blogDraft.toHeader())
                    return blogDraft
                })
            })
        },
        loadBlogDraft(store: ActionContext<MyContentState, any>, blogID: number) {
            return blogClient.getBlogDraftByID(blogID).then((blogDraft) => {
                store.commit("workOnBlog", blogDraft)
                if (blogDraft.published) {
                    return blogClient.getBlogByID(blogID).then((blog) => {
                        store.commit("setPublishedBlog", blog)
                        return blogDraft
                    })
                } else {
                    store.commit("setPublishedBlog", null)
                    return blogDraft
                }
            })
        },
        updateBlogDraftAttribute(store: ActionContext<MyContentState, any>, payload: { attributeName: string, value: any }) {
            store.commit("updateBlogDraftAttribute", payload)
        },
        saveBlogDraft(store: ActionContext<MyContentState, any>) {
            return blogClient.updateBlogDraft(store.getters.modifiedBlogDraft).then((blogDraft) => {
                store.commit("workOnBlog", blogDraft)
                store.commit("refreshBlogHeader", blogDraft.toHeader())
                return blogDraft
            })
        }
    }
    return actions
}

/**
 * Constructor function that allows injection of the user client.
 * @param userClient
 */
const createStore = function (blogClient: Blogs): any {
    return {
        state: new MyContentState(),
        getters: getters,
        mutations: mutations,
        actions: buildActions(blogClient)
    }
}

const client = function (): Blogs {
    if (isDev()) {
        // return new BlogsClientFake()
        return new BlogsClient()
    }
    return new BlogsClient()
}

export const MyContentStore = createStore(client())
