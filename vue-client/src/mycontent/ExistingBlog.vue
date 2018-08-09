<template>
    <div>
        <v-loading :is-loading="!loaded">
            <b-row>
                <b-col>{{status}}</b-col>
            </b-row>
            <b-row>
                <b-col>
                    <blog :title="title" :content="content"></blog>
                </b-col>
            </b-row>
        </v-loading>
    </div>
</template>

<script lang="ts">
    import Vue from "vue"
    import {Component, Watch} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import {BlogDraft} from "@/blogs/api"
    import Blog from "@/blogs/Blog.vue"
    import VLoading from "@/components/VLoading.vue"

    @Component({
        components: {VLoading, Blog}
    })
    export default class ExistingBlog extends Vue {
        loaded = false

        @Getter blogDraft?: BlogDraft
        @Action loadBlogDraft

        mounted() {
            this.loaded = false
            const blogID = Number(this.$route.params.id)
            this.loadBlogDraft(blogID).then((blogDraft) => {
                this.loaded = true
            })
        }

        @Watch("$route")
        routeChanged() {
            this.loaded = false
            const blogID = Number(this.$route.params.id)
            this.loadBlogDraft(blogID).then((blogDraft) => {
                this.loaded = true
            })
        }

        get title(): string {
            return this.blogDraft ? this.blogDraft.title : ""
        }

        get content(): string {
            return this.blogDraft ? this.blogDraft.content : ""
        }

        get status(): string {
            if (this.blogDraft) {
                if (this.blogDraft.published && this.blogDraft.unpublishedChanges) {
                    return "Unpublished Changes"
                }
                if (!this.blogDraft.unpublishedChanges) {
                    return "Published"
                }
                return "Draft"
            }
            return "Unknown"
        }

    }
</script>