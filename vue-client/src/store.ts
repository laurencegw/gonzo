import Vue from "vue"
import Vuex from "vuex"
import {UserStore} from "@/users/store"
import {MyContentStore} from "@/mycontent/store"
import {ArticlesStore} from "@/stores/ArticlesStore"

Vue.use(Vuex)

const store = new Vuex.Store({
    modules: {
        UserStore,
        MyContentStore,
        ArticlesStore
    }
})

export default store
