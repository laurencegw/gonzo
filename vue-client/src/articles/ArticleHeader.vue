<template>
    <div @click="click">
        <div>{{header.title}}</div>
        <div>Published: {{ header.created | fDate }}</div>
        <div>Updated: {{header.updated | fDate }}</div>
        <div>Author: {{ header.author.handle }}</div>
    </div>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop, Emit} from "vue-property-decorator"
    import {ArticleHeader} from "@/articles/api"

    @Component({})
    export default class ArticletHeader extends Vue {
        @Prop() header!: ArticleHeader

        @Emit()
        click(event) {

        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>