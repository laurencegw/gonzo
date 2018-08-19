<template>
    <div>
        <v-hunter-quote>So we shall let the reader answer this question for himself: who is the happier man, he who has
            braved the storm of life and lived or he who has stayed securely on shore and merely existed?
        </v-hunter-quote>
        <article-header-list :headers="headers" @header-clicked="headerClicked"></article-header-list>
    </div>
</template>
<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import VHunterQuote from "@/components/VHunterQuote.vue"
    import {ArticleHeader} from "../articles/api"
    import {UserPublicHeader} from "../users/api"
    import ArticleHeaderList from "../articles/ArticleHeaderList.vue"

    @Component({
        components: {ArticleHeaderList, VHunterQuote}
    })
    export default class FrontPage extends Vue {

        headers: Array<ArticleHeader> = []

        mounted() {
            for (let i = 0; i < 5; i++) {
                this.headers.push(
                    new ArticleHeader({
                        id: i,
                        title: `Title ${i}`,
                        created: Date(),
                        updated: Date(),
                        author: new UserPublicHeader(
                            i,
                            "handle",
                            "First",
                            "Last"
                        )
                    })
                )
            }
        }

        headerClicked(header: ArticleHeader) {
            console.log(header.title)
        }

    }
</script>