<template>
    <div>
        <v-loading :is-loading="isLoading">

        </v-loading>
        <v-hunter-quote>So we shall let the reader answer this question for himself: who is the happier man, he who has
            braved the storm of life and lived or he who has stayed securely on shore and merely existed?
        </v-hunter-quote>
        <article-header-list :headers="articleHeaders" @header-clicked="headerClicked"></article-header-list>
    </div>
</template>
<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import VHunterQuote from "@/components/VHunterQuote.vue"
    import {ArticleHeader} from "@/articles/api"
    import ArticleHeaderList from "@/articles/ArticleHeaderList.vue"
    import VLoading from "@/components/VLoading.vue"

    @Component({
        components: {VLoading, ArticleHeaderList, VHunterQuote}
    })
    export default class FrontPage extends Vue {

        loaded = false

        @Getter articleHeaders!: Array<ArticleHeader>

        @Action loadAllArticleHeaders

        mounted() {
            this.loadAllArticleHeaders().then(() => {
                this.loaded = true
            })
        }

        headerClicked(header: ArticleHeader) {
            console.log(header.title)
        }

        get isLoading(): boolean {
            return !this.loaded
        }

    }
</script>