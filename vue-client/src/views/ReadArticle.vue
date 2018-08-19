<template>
    <v-loading :is-loading="isLoading">
        <published-article
                :article="article"
        ></published-article>
    </v-loading>
</template>
<script lang="ts">
    import {Component, Vue, Watch} from "vue-property-decorator"
    import {Article} from "@/articles/api"
    import VLoading from "@/components/VLoading.vue"
    import {ArticlesClient} from "@/articles/clients"
    import PublishedArticle from "@/articles/PublishedArticle.vue"

    @Component({
        components: {PublishedArticle, VLoading}
    })
    export default class ReadArticle extends Vue {

        loaded = false
        article: Article | null = null
        client = new ArticlesClient()

        get isLoading(): boolean {
            return !this.loaded
        }

        mounted() {
            console.log("mounted")
            this.load()
        }

        @Watch("$route")
        routeChanged() {
            this.load()
        }

        load() {
            this.loaded = false
            this.article = null
            const articleID = Number(this.$route.params.id)
            this.client.getArticleByID(articleID).then((article: Article) => {
                this.article = article
                this.loaded = true
            })
        }
    }
</script>