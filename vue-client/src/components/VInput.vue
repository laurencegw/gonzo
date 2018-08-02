<template>
    <input
            :value="value"
            id="title-input"
            type="text"
            @change="change"
            @keypress.capture="keyPressed"
    >
</template>

<script lang="ts">
    import {Component, Emit, Vue, Prop} from "vue-property-decorator"

    @Component({
        name: "v-input"
    })
    export default class VInput extends Vue {
        @Prop() value = ""
        @Prop() name!: string

        change(event) {
            this.$emit("change", {name: this.name, value: event.targget.value})
        }

        keyPressed(event) {
            console.log("Custom key press")
            this.$emit("keypress", {name: this.name, value: event.target.value})
        }
    }
</script>

<style scoped lang="scss">
    @import "@/gonzo_style.scss";
</style>