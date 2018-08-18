<template>
    <b-container>
        <blog-content :title="title" :content="content"></blog-content>
    </b-container>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop} from "vue-property-decorator"
    import BlogContent from "./BlogContent"

    @Component({
        components: {BlogContent}
    })
    export default class Blog extends Vue {
        @Prop() title!: string
        @Prop() content!: string
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>