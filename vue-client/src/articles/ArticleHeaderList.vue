<template>
    <b-container>
        <v-list :items="headers">
            <template slot-scope="slotProps">
                <article-header :header="slotProps.item" @click="headerClicked(slotProps.item)"></article-header>
            </template>
        </v-list>
    </b-container>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop, Emit} from "vue-property-decorator"
    import {ArticleHeader} from "@/articles/api"
    import VList from "../components/VList.vue"
    import ArticleHeaderComponent from "./ArticleHeader.vue"

    @Component({
        components: {"article-header": ArticleHeaderComponent, VList}
    })
    export default class ArticleHeaderList extends Vue {
        @Prop() headers!: Array<ArticleHeader>

        @Emit()
        headerClicked(header: ArticleHeader) {
        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>