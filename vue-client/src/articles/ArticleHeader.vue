<template>
    <b-container @click="click" class="margined bordered-light-thin">
        <b-row>
            <b-col><h4>{{header.title}}</h4></b-col>
        </b-row>
        <b-row>
            <b-col md="auto">@{{header.author.handle}}</b-col>
            <b-col md="auto">{{header.created | fDate}}</b-col>
            <b-col md="auto" v-if="showUpdateDate">Updated: {{header.updated | fDate}}</b-col>
        </b-row>
    </b-container>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop, Emit} from "vue-property-decorator"
    import {ArticleHeader} from "@/articles/api"

    @Component({})
    export default class ArticleHeaderComponent extends Vue {
        @Prop() header!: ArticleHeader

        @Emit()
        click(event) {

        }

        get showUpdateDate(): boolean {
            return this.header ? this.header.created !== this.header.updated : false
        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>