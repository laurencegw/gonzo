import Vue from "vue"
import Router from "vue-router"
import Tags from "./views/Tags.vue"
import FrontPage from "./views/FrontPage.vue"
import Authors from "./views/Authors.vue"
import Me from "./views/Me.vue"

Vue.use(Router)

export default new Router({
    routes: [
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
            path: "/authors",
            name: "authors",
            component: Authors,
        },
        {
            path: "/me",
            name: "me",
            component: Me,
        }
    ],
})
