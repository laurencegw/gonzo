import {Blog, BlogDraft, BlogDraftNew, BlogHeader, Blogs} from "@/blogs/api"
import {ActionContext, ActionTree, GetterTree, MutationTree} from "vuex"
import {cloneDeep} from "lodash"
import {isDev} from "@/utils"
import {BlogsClient, BlogsClientFake} from "@/blogs/clients"

class MyContentState {
    blogHeadersByID: {[id: number]: BlogHeader} = {}
    blogIDs: Array<number> = []
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
    }
}

const mutations: MutationTree<MyContentState> = {
    setBlogHeaders(state: MyContentState, blogHeaders: Array<BlogHeader>) {
        const IDs: Array<number> = []
        const headersByID: {[id: number]: BlogHeader} = {}
        for (const header of blogHeaders) {
            IDs.push(header.id)
            headersByID[header.id] = header
        }
        state.blogIDs = IDs
        state.blogHeadersByID = headersByID
    },
    workOnBlog(state: MyContentState, blog: BlogDraft) {
        state.blogDraft = cloneDeep(blog)
        state.modifiedBlogDraft = cloneDeep(blog)
    },
}

const buildActions = function (blogClient: Blogs): ActionTree<MyContentState, any> {
    const actions: ActionTree<MyContentState, any> = {
        loadDraftHeadersForAuthor(store: ActionContext<MyContentState, any>, authorID: number) {
            return blogClient.getBlogDraftHeaders(authorID).then((headers) => {
                store.commit("setBlogHeaders", headers)
            })
        },
        createBlog(store:  ActionContext<MyContentState, any>, newBlog: BlogDraftNew) {
            return blogClient.createBlogDraft(newBlog).then((blogDraft) => {
                store.commit("addBlogHeader", blogDraft.toHeader())
                store.commit("workOnBlog", blogDraft)
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
