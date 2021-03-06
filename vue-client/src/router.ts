import Vue from "vue"
import Router from "vue-router"
import Tags from "@/views/Tags.vue"
import FrontPage from "@/views/FrontPage.vue"
import Authors from "@/views/Coders.vue"
import Me from "@/views/Me.vue"
import MyContent from "@/views/MyContent.vue"
import Login from "@/views/Login.vue"
import NewArticle from "@/mycontent/NewArticle.vue"
import ExistingArticle from "@/mycontent/ExistingArticle.vue"
import EditArticle from "@/mycontent/EditArticle.vue"
import ReadArticle from "@/views/ReadArticle.vue"
import store from "@/store"
import {LoginState} from "@/users/store"

Vue.use(Router)

function loginGuard(from, to, next) {
    return store.dispatch("checkLoginState").then(() => {
        if (store.getters.loginState !== LoginState.LOGGED_IN) {
            next({name: "frontpage"})
        } else {
            next()
        }
    })
}

export default new Router({
    routes: [
        {
            path: "/",
            redirect: {name: "frontpage"}
        },
        {
            path: "/tags",
            name: "tags",
            component: Tags,
        },
        {
            path: "/frontpage",
            name: "frontpage",
            component: FrontPage,
        },
        {
            path: "/coders",
            name: "coders",
            component: Authors,
        },
        {
            path: "/me",
            name: "me",
            component: Me,
            beforeEnter: loginGuard
        },
        {
            path: "/read/:id",
            name: "read",
            component: ReadArticle,
        },
        {
            path: "/my-content",
            name: "my-content",
            component: MyContent,
            beforeEnter: loginGuard,
            children: [
                {
                    path: "new-entry",
                    name: "new-entry",
                    component: NewArticle,
                },
                {
                    path: "draft/:id",
                    name: "draft",
                    component: ExistingArticle,
                },
                {
                    path: "edit/:id",
                    name: "edit",
                    component: EditArticle,
                },
            ]
        },
        {
            path: "/login",
            name: "login",
            component: Login,
            beforeEnter: (from, to, next) => {
                if (store.getters.loginState === LoginState.LOGGED_IN) {
                    next({name: "frontpage"})
                } else {
                    next()
                }
            }
        }
    ],
})
