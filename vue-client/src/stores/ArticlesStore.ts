import {ArticleHeader} from "@/articles/api"
import {GetterTree, MutationTree} from "vuex"
import {cloneDeep} from "lodash"
import Vue from "vue"

class ArticlesState {
    articleHeadersByID: { [id: number]: ArticleHeader } = {}
    articleIDs: Array<number> = []
}

const getters: GetterTree<ArticlesState, any> = {
    articleIDs(state: ArticlesState): Array<number> {
        return cloneDeep(state.articleIDs)
    },
    articleHeader(state: ArticlesState, articleID: number): ArticleHeader {
        return cloneDeep(state.articleHeadersByID[articleID])
    },
    articleHeaders(state: ArticlesState): Array<ArticleHeader> {
        return cloneDeep(state.articleIDs.map(id => state.articleHeadersByID[id]))
    }
}

const mutations: MutationTree<ArticlesState> = {
    setArticleHeaders(state: ArticlesState, articleHeaders: Array<ArticleHeader>) {
        const IDs: Array<number> = []
        const headersByID: { [id: number]: ArticleHeader } = {}
        for (const header of articleHeaders) {
            IDs.push(header.id)
            headersByID[header.id] = header
        }
        Vue.set(state, "articleIDs", IDs)
        Vue.set(state, "articleHeadersByID", headersByID)
    },
    refreshArticleHeader(state: ArticlesState, articleHeader: ArticleHeader) {
        const headersByID: { [id: number]: ArticleHeader } = state.articleHeadersByID
        headersByID[articleHeader.id] = articleHeader
        Vue.set(state, "articleHeadersByID", headersByID)
    },
    addArticleHeader(state: ArticlesState, articleHeader: ArticleHeader) {
        state.articleIDs.unshift(articleHeader.id)
        Vue.set(state, "articleIDs", state.articleIDs)
        state.articleHeadersByID[articleHeader.id] = articleHeader
        Vue.set(state, "articleHeadersByID", state.articleHeadersByID)
    },
    removeArticle(state: ArticlesState, articleID) {
        const currentIDs = state.articleIDs
        const index = currentIDs.indexOf(articleID)
        if (index !== -1) {
            currentIDs.splice(index, 1)
        }
        Vue.set(state, "articleIDs", currentIDs)
    }
}

export const ArticlesStore = {
    state: new ArticlesState(),
    getters: getters,
    mutations: mutations,
}
