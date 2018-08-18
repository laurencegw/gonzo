<template>
    <div>
        <v-loading :is-loading="!loaded">
            <b-row>
                <b-col md="2">{{status}}</b-col>
                <b-col v-if="unpublishedChanges" md="1"><input type="checkbox" v-model="showDraft"></b-col>
            </b-row>
            <b-row>
                <b-col>
                    <article :title="title" :content="content"></article>
                </b-col>
            </b-row>
        </v-loading>
    </div>
</template>

<script lang="ts">
    import Vue from "vue"
    import {Component, Watch} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import {ArticleDraft} from "@/articles/api"
    import Article from "@/articles/ArticleContent.vue"
    import VLoading from "@/components/VLoading.vue"

    @Component({
        components: {VLoading, Article}
    })
    export default class ExistingArticle extends Vue {
        loaded = false
        showDraft = true

        @Getter articleDraft?: ArticleDraft
        @Getter publishedArticle?: Article
        @Action loadArticleDraft

        mounted() {
            this.load()
        }

        @Watch("$route")
        routeChanged() {
            this.load()
        }

        load() {
            this.loaded = false
            const articleID = Number(this.$route.params.id)
            this.loadArticleDraft(articleID).then((articleDraft) => {
                this.loaded = true
                if (!this.articleDraft!.published) {
                    this.showDraft = true
                }
            })
        }

        get title(): string {
            if (this.showDraft) {
                return this.articleDraft ? this.articleDraft.title : ""
            }
            return this.publishedArticle ? this.publishedArticle.title : ""
        }

        get content(): string {
            if (this.showDraft) {
                return this.articleDraft ? this.articleDraft.content : ""
            }
            return this.publishedArticle ? this.publishedArticle.content : ""
        }

        get status(): string {
            if (this.articleDraft) {
                if (this.articleDraft.published && this.articleDraft.unpublishedChanges) {
                    return "Changes"
                }
                if (!this.articleDraft.unpublishedChanges) {
                    return "Published"
                }
                return "Draft"
            }
            return "Unknown"
        }

        get unpublishedChanges(): Boolean {
            return this.status === "Changes"
        }

    }
</script>