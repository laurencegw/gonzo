import Vue from "vue"
import Router from "vue-router"
import Tags from "@/views/Tags.vue"
import FrontPage from "@/views/FrontPage.vue"
import Authors from "@/views/Coders.vue"
import Me from "@/views/Me.vue"
import MyContent from "@/views/MyContent.vue"
import Login from "@/views/Login.vue"
import NewEntry from "@/mycontent/NewEntry.vue"
import store from "@/store"
import {LoginState} from "@/users/store"

Vue.use(Router)

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
            beforeEnter: (from, to, next) => {
                if (store.getters.loginState !== LoginState.LOGGED_IN) {
                    next({name: "frontpage"})
                } else {
                    next()
                }
            }
        },
        {
            path: "/my-content",
            name: "my-content",
            component: MyContent,
            beforeEnter: (from, to, next) => {
                if (store.getters.loginState !== LoginState.LOGGED_IN) {
                    next({name: "frontpage"})
                } else {
                    next()
                }
            },
            children: [
                {
                    path: "new-entry",
                    name: "new-entry",
                    component: NewEntry,
                }
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
