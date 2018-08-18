import {Article, ArticleDraft, ArticleDraftNew, ArticleHeader, Articles} from "@/articles/api"
import {ActionContext, ActionTree, GetterTree, MutationTree} from "vuex"
import {cloneDeep} from "lodash"
import {isDev} from "@/utils"
import {ArticlesClient} from "@/articles/clients"
import Vue from "vue"

class MyContentState {
    articleHeadersByID: { [id: number]: ArticleHeader } = {}
    articleIDs: Array<number> = []
    publishedArticle?: Article
    articleDraft?: ArticleDraft
    modifiedArticleDraft?: ArticleDraft
}

const getters: GetterTree<MyContentState, any> = {
    articleIDs(state: MyContentState): Array<number> {
        return cloneDeep(state.articleIDs)
    },
    articleHeader(state: MyContentState, articleID: number): ArticleHeader {
        return cloneDeep(state.articleHeadersByID[articleID])
    },
    articleHeaders(state: MyContentState): Array<ArticleHeader> {
        return cloneDeep(state.articleIDs.map(id => state.articleHeadersByID[id]))
    },
    articleDraft(state: MyContentState): ArticleDraft | undefined {
        return cloneDeep(state.articleDraft)
    },
    modifiedArticleDraft(state: MyContentState): ArticleDraft | undefined {
        return cloneDeep(state.modifiedArticleDraft)
    },
    publishedArticle(state: MyContentState): Article | undefined {
        return cloneDeep(state.publishedArticle)
    }
}

const mutations: MutationTree<MyContentState> = {
    setArticleHeaders(state: MyContentState, articleHeaders: Array<ArticleHeader>) {
        const IDs: Array<number> = []
        const headersByID: { [id: number]: ArticleHeader } = {}
        for (const header of articleHeaders) {
            IDs.push(header.id)
            headersByID[header.id] = header
        }
        Vue.set(state, "articleIDs", IDs)
        Vue.set(state, "articleHeadersByID", headersByID)
    },
    refreshArticleHeader(state: MyContentState, articleHeader: ArticleHeader) {
        const headersByID: { [id: number]: ArticleHeader } = state.articleHeadersByID
        headersByID[articleHeader.id] = articleHeader
        Vue.set(state, "articleHeadersByID", headersByID)
    },
    workOnArticle(state: MyContentState, article: ArticleDraft) {
        Vue.set(state, "articleDraft", cloneDeep(article))
        Vue.set(state, "modifiedArticleDraft", cloneDeep(article))
    },
    setPublishedArticle(state: MyContentState, article: Article) {
        Vue.set(state, "publishedArticle", article)
    },
    addArticleHeader(state: MyContentState, articleHeader: ArticleHeader) {
        state.articleIDs.unshift(articleHeader.id)
        Vue.set(state, "articleIDs", state.articleIDs)
        state.articleHeadersByID[articleHeader.id] = articleHeader
        Vue.set(state, "articleHeadersByID", state.articleHeadersByID)
    },
    updateArticleDraftAttribute(state: MyContentState, payload: { attributeName: string, value: any }) {
        Vue.set(state.modifiedArticleDraft!, payload.attributeName, payload.value)
    },
    deleteArticle(state: MyContentState, articleID) {
        const currentIDs = state.articleIDs
        const index = currentIDs.indexOf(articleID)
        if (index !== -1) {
            currentIDs.splice(index, 1)
        }
        Vue.set(state, "articleIDs", currentIDs)
    }
}

const buildActions = function (articleClient: Articles): ActionTree<MyContentState, any> {
    const actions: ActionTree<MyContentState, any> = {
        loadDraftHeadersForAuthor(store: ActionContext<MyContentState, any>, authorID: number) {
            return articleClient.getArticleDraftHeaders(authorID).then((headers) => {
                store.commit("setArticleHeaders", headers)
                return headers
            })
        },
        createArticle(store: ActionContext<MyContentState, any>, newArticle: ArticleDraftNew) {
            return articleClient.createArticleDraft(newArticle).then((articleDraft) => {
                store.commit("addArticleHeader", articleDraft.toHeader())
                store.commit("workOnArticle", articleDraft)
                return articleDraft
            })
        },
        deleteArticle(store: ActionContext<MyContentState, any>) {
            const currentArticleID = store.getters.articleDraft.id
            return articleClient.deleteArticle(currentArticleID).then((articleDraft) => {
                store.commit("deleteArticle", currentArticleID)
                store.commit("workOnArticle", null)
            })
        },
        publishArticle(store: ActionContext<MyContentState, any>) {
            const currentArticleID = store.getters.articleDraft.id
            return articleClient.publishArticle(currentArticleID).then(() => {
                return store.dispatch("loadArticleDraft", currentArticleID).then((articleDraft: ArticleDraft) => {
                    store.commit("refreshArticleHeader", articleDraft.toHeader())
                    return articleDraft
                })
            })
        },
        loadArticleDraft(store: ActionContext<MyContentState, any>, articleID: number) {
            return articleClient.getArticleDraftByID(articleID).then((articleDraft) => {
                store.commit("workOnArticle", articleDraft)
                if (articleDraft.published) {
                    return articleClient.getArticleByID(articleID).then((article) => {
                        store.commit("setPublishedArticle", article)
                        return articleDraft
                    })
                } else {
                    store.commit("setPublishedArticle", null)
                    return articleDraft
                }
            })
        },
        updateArticleDraftAttribute(store: ActionContext<MyContentState, any>, payload: { attributeName: string, value: any }) {
            store.commit("updateArticleDraftAttribute", payload)
        },
        saveArticleDraft(store: ActionContext<MyContentState, any>) {
            return articleClient.updateArticleDraft(store.getters.modifiedArticleDraft).then((articleDraft) => {
                store.commit("workOnArticle", articleDraft)
                store.commit("refreshArticleHeader", articleDraft.toHeader())
                return articleDraft
            })
        }
    }
    return actions
}

/**
 * Constructor function that allows injection of the user client.
 * @param userClient
 */
const createStore = function (articleClient: Articles): any {
    return {
        state: new MyContentState(),
        getters: getters,
        mutations: mutations,
        actions: buildActions(articleClient)
    }
}

const client = function (): Articles {
    if (isDev()) {
        // return new ArticlesClientFake()
        return new ArticlesClient()
    }
    return new ArticlesClient()
}

export const MyContentStore = createStore(client())
