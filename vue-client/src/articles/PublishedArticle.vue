<template>
    <b-container>
        <article-content :title="title" :content="content"></article-content>
    </b-container>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop} from "vue-property-decorator"
    import ArticleContent from "@/articles/ArticleContent.vue"

    @Component({
        components: {ArticleContent}
    })
    export default class Article extends Vue {
        @Prop() title!: string
        @Prop() content!: string
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>