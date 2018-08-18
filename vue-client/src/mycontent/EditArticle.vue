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
    import {ArticleDraft} from "@/articles/api"
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
    export default class EditArticle extends Vue {
        loaded = false

        @Getter modifiedArticleDraft?: ArticleDraft
        @Action loadArticleDraft
        @Action updateArticleDraftAttribute
        @Action saveArticleDraft

        mounted() {
            this.loaded = false
            const articleID = Number(this.$route.params.id)
            this.loadArticleDraft(articleID).then((articleDraft) => {
                this.loaded = true
            })
        }

        @Watch("$route")
        routeChanged() {
            this.loaded = false
            const articleID = Number(this.$route.params.id)
            this.loadArticleDraft(articleID).then((articleDraft) => {
                this.loaded = true
            })
        }

        get title(): string {
            return this.modifiedArticleDraft ? this.modifiedArticleDraft.title : ""
        }

        get content(): string {
            return this.modifiedArticleDraft ? this.modifiedArticleDraft.content : ""
        }

        attributeChange(change: { attributeName: string, value: any }) {
            this.updateArticleDraftAttribute(change)
        }

        save() {
            this.saveArticleDraft()
        }

    }
</script>

<style scoped>

</style>