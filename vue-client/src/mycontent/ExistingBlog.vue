<template>
    <div>
        <v-loading :is-loading="!loaded">
            <b-row>
                <b-col md="2">{{status}}</b-col>
                <b-col v-if="unpublishedChanges" md="1"><input type="checkbox" v-model="showDraft"></b-col>
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
        showDraft = true

        @Getter blogDraft?: BlogDraft
        @Getter publishedBlog?: Blog
        @Action loadBlogDraft

        mounted() {
            this.load()
        }

        @Watch("$route")
        routeChanged() {
            this.load()
        }

        load() {
            this.loaded = false
            const blogID = Number(this.$route.params.id)
            this.loadBlogDraft(blogID).then((blogDraft) => {
                this.loaded = true
                if (!this.blogDraft!.published) {
                    this.showDraft = true
                }
            })
        }

        get title(): string {
            if (this.showDraft) {
                return this.blogDraft ? this.blogDraft.title : ""
            }
            return this.publishedBlog ? this.publishedBlog.title : ""
        }

        get content(): string {
            if (this.showDraft) {
                return this.blogDraft ? this.blogDraft.content : ""
            }
            return this.publishedBlog ? this.publishedBlog.content : ""
        }

        get status(): string {
            if (this.blogDraft) {
                if (this.blogDraft.published && this.blogDraft.unpublishedChanges) {
                    return "Changes"
                }
                if (!this.blogDraft.unpublishedChanges) {
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