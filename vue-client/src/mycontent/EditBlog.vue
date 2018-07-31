<template>
  <v-loading :is-loading="!loaded">
    <b-container fluid>
      <b-row>
        <b-col md="1"><label for="title-input">Title:</label></b-col>
        <b-col md="9"><input :value="title" id="title-input" type="text" @change="change"></b-col>
        <b-col md="2">
          <v-button @click="save()">Save</v-button>
        </b-col>
      </b-row>
      <br>
      <b-row><textarea :value="content" @change="change" rows="15" cols="80"></textarea></b-row>
    </b-container>
  </v-loading>

</template>
<script lang="ts">
    import Vue from "vue"
    import {Component, Watch} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import VButton from "@/components/VButton.vue"
    import {BlogDraft} from "@/blogs/api"
    import VLoading from "@/components/VLoading.vue"

    @Component({
        components: {
            VButton,
            VLoading
        }
    })
    export default class EditBlog extends Vue {
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

        change(target: { name: string, value: any }) {
            console.log(target)
        }

        save() {
            console.log("Saved")
        }

    }
</script>

<style scoped>

</style>