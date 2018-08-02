<template>
  <v-loading :is-loading="!loaded">
    <b-container fluid>
      <b-row>
        <b-col md="1"><label for="title-input">Title:</label></b-col>
        <b-col md="9"><v-input :value="title" id="title-input" name="title" type="text" @keypress="change"></v-input></b-col>
        <b-col md="2">
          <v-button @click="save()">Save</v-button>
        </b-col>
      </b-row>
      <br>
      <b-row><textarea :value="content" @keypress="change" rows="15" cols="80"></textarea></b-row>
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
    import VInput from "@/components/VInput.vue"

    @Component({
        components: {
            VButton,
            VLoading,
            VInput
        }
    })
    export default class EditBlog extends Vue {
        loaded = false

        @Getter modifiedBlogDraft?: BlogDraft
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
            return this.modifiedBlogDraft ? this.modifiedBlogDraft.title : ""
        }

        get content(): string {
            return this.modifiedBlogDraft ? this.modifiedBlogDraft.content : ""
        }

        change(event) {
            console.log(event)
        }

        save() {
            console.log("Saved")
        }

    }
</script>

<style scoped>

</style>