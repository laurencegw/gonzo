<template>
    <b-container>
        <b-row v-for="(item, index) in items">
            <b-col>
                <slot :item="item" :index="index">
                    <!--Default Content-->
                    {{ item }}
                </slot>
            </b-col>
        </b-row>
    </b-container>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop} from "vue-property-decorator"

    @Component({})
    export default class VList extends Vue {
        @Prop() items!: Array<any>
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>