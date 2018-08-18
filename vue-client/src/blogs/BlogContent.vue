<template>
    <div>
        <b-row>
            <b-col><h2>{{title}}</h2></b-col>
        </b-row>
        <b-row>
            <b-col>
                <vue-markdown :source="content"></vue-markdown>
            </b-col>
        </b-row>
    </div>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop} from "vue-property-decorator"
    import VueMarkdown from "vue-markdown"

    @Component({
        components: {"vue-markdown": VueMarkdown}
    })
    export default class BlogContent extends Vue {
        @Prop() title!: string
        @Prop() content!: string
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>