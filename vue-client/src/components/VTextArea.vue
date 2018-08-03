<template>
  <textarea
      :value="value"
      :rows="rows"
      :cols="cols"
      @keyup="keyUp"
  ></textarea>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator"

    @Component({
        name: "v-text-area"
    })
    export default class VTextArea extends Vue {
        @Prop({default: ""})
        value!: string

        @Prop({default: "unknown"})
        attributeName!: string

        @Prop({default: "5"})
        rows!: string

        @Prop({default: "50"})
        cols!: string

        keyUp(event) {
            this.$emit("keyup", {attributeName: this.attributeName, value: event.target.value})
        }
    }
</script>

<style scoped lang="scss">
  @import "@/gonzo_style.scss";
</style>