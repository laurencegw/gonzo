import {Articles} from "@/articles/api"
import {ActionContext, ActionTree} from "vuex"
import {cloneDeep} from "lodash"
import {isDev} from "@/utils"
import {ArticlesClient} from "@/articles/clients"

class FrontPageState {
}

const buildActions = function (articleClient: Articles): ActionTree<FrontPageState, any> {
    const actions: ActionTree<FrontPageState, any> = {
        loadAllArticleHeaders(store: ActionContext<FrontPageState, any>, authorID: number) {
            return articleClient.getAllArticleHeaders().then((headers) => {
                store.commit("setArticleHeaders", headers)
                return headers
            })
        }
    }
    return actions
}

/**
 * Constructor function that allows injection of the client.
 * @param articleClient
 */
const createStore = function (articleClient: Articles): any {
    return {
        state: new FrontPageState(),
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

export const FrontPageStore = createStore(client())

