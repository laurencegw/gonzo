<template>
    <b-container>
        <b-container>
            <b-row>
                <b-col><h1>{{article.title}}</h1></b-col>
            </b-row>
            <b-row>
                <b-col md="auto">@{{article.author.handle}}</b-col>
                <b-col md="auto">{{article.publishedOn | fDate}}</b-col>
                <b-col md="auto" v-if="showUpdated">Updated: {{article.lastEdited | fDate}}</b-col>
            </b-row>
            <br>
            <b-row>
                <b-col>
                    <vue-markdown :source="article.content"></vue-markdown>
                </b-col>
            </b-row>
        </b-container>
    </b-container>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop} from "vue-property-decorator"
    import {Article} from "./api"
    import VueMarkdown from "vue-markdown"

    @Component({
        components: {"vue-markdown": VueMarkdown}
    })
    export default class ArticleComponent extends Vue {
        @Prop() article!: Article

        get showUpdated(): boolean {
            return this.article.publishedOn !== this.article.lastEdited
        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>