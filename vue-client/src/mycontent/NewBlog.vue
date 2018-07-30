<template>
  <b-container fluid>
    <b-row>
      <b-col md="1"><label for="title-input">Title:</label></b-col>
      <b-col md="9"><input v-model="title" id="title-input" type="text"></b-col>
      <b-col md="2">
        <v-button @click="save()">Create</v-button>
      </b-col>
    </b-row>
    <br>
    <b-row><textarea v-model="content" rows="15" cols="80"></textarea></b-row>
  </b-container>

</template>
<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import VButton from "@/components/VButton.vue"
    import {BlogDraftNew} from "../blogs/api"

    @Component({
        components: {
            VButton
        }
    })
    export default class NewEntry extends Vue {

        title = ""
        content = ""

        save() {
            const userID = this.$store.getters.user.id
            this.$store.dispatch("createBlog", new BlogDraftNew(
                this.title,
                this.content,
                userID
            )).then(blogDraft => {
                console.log(blogDraft)
                this.$router.push({name: "draft", params: {id: blogDraft.id}})
            })
        }

    }
</script>

<style scoped>

</style>