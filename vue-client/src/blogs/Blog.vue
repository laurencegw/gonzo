<template>
    <div>
        <b-row>
            <b-col>{{title}}</b-col>
        </b-row>
        <b-row>
            <b-col>
                <vue-markdown>{{content}}</vue-markdown>
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
    export default class Blog extends Vue {
        @Prop() title!: string
        @Prop() content!: string
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>