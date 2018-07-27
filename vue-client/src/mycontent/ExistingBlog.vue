<template>
  <div>
    <div v-if="loaded">
      <blog :title="title" :content="content"></blog>
    </div>
    <div v-else>Loading...</div>
  </div>
</template>

<script lang="ts">
    import Vue from "vue"
    import {Component, Watch} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import {BlogDraft} from "@/blogs/api"
    import Blog from "@/blogs/Blog.vue"

    @Component({
        components: {Blog}
    })
    export default class ExistingBlog extends Vue {
        loaded = false

        @Getter blogDraft?: BlogDraft
        @Action loadBlogDraft

        mounted() {
            console.log("Mounted3")
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
            console.log(`Route changed ${blogID}`)
            this.loadBlogDraft(blogID).then((blogDraft) => {
                this.loaded = true
                console.log(`component got ${blogDraft.id}`)
            })
        }

        get title(): string {
            return this.blogDraft ? this.blogDraft.title : ""
        }

        get content(): string {
            return this.blogDraft ? this.blogDraft.content : ""
        }

    }
</script>