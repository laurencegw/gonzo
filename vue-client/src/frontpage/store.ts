import {Article, ArticleDraft, ArticleHeader} from "@/articles/api"
import {GetterTree, MutationTree} from "vuex"
import {cloneDeep} from "lodash"
import Vue from "vue"


class MyFrontPageState {
    articleHeadersByID: { [id: number]: ArticleHeader } = {}
    articleIDs: Array<number> = []
}

const getters: GetterTree<MyFrontPageState, any> = {
    articleIDs(state: MyFrontPageState): Array<number> {
        return cloneDeep(state.articleIDs)
    },
    articleHeader(state: MyFrontPageState, articleID: number): ArticleHeader {
        return cloneDeep(state.articleHeadersByID[articleID])
    },
    articleHeaders(state: MyFrontPageState): Array<ArticleHeader> {
        return cloneDeep(state.articleIDs.map(id => state.articleHeadersByID[id]))
    },
}

const mutations: MutationTree<MyFrontPageState> = {
    setArticleHeaders(state: MyFrontPageState, articleHeaders: Array<ArticleHeader>) {
        const IDs: Array<number> = []
        const headersByID: { [id: number]: ArticleHeader } = {}
        for (const header of articleHeaders) {
            IDs.push(header.id)
            headersByID[header.id] = header
        }
        Vue.set(state, "articleIDs", IDs)
        Vue.set(state, "articleHeadersByID", headersByID)
    }
}
