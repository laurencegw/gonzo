<template>
    <v-loading :is-loading="!loaded">
        <b-container fluid>
            <b-row>
                <b-col md="1"><label for="title-input">Title:</label></b-col>
                <b-col md="9">
                    <v-input
                            attribute-name="title"
                            :value="title"
                            id="title-input"
                            type="text"
                            @keyup="attributeChange"
                    ></v-input>
                </b-col>
                <b-col md="2">
                    <v-button @click="save()">Save</v-button>
                </b-col>
            </b-row>
            <br>
            <b-row>
                <v-text-area
                        attribute-name="content"
                        :value="content"
                        @keyup="attributeChange"
                        rows=15
                        cols=80
                ></v-text-area>
            </b-row>
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
    import VTextArea from "../components/VTextArea.vue"

    @Component({
        components: {
            VTextArea,
            VButton,
            VLoading,
            VInput
        }
    })
    export default class EditBlog extends Vue {
        loaded = false

        @Getter modifiedBlogDraft?: BlogDraft
        @Action loadBlogDraft
        @Action updateBlogDraftAttribute

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

        attributeChange(change: { attributeName: string, value: any }) {
            this.updateBlogDraftAttribute(change)
        }

        save() {
            console.log("Saved")
        }

    }
</script>

<style scoped>

</style>