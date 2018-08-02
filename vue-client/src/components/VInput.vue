<template>
  <input
      :value="value"
      id="title-input"
      type="text"
      @keyup="keyUp"
  >
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator"

    @Component({
        name: "v-input"
    })
    export default class VInput extends Vue {
        @Prop({default: ""})
        value!: string

        @Prop({default: "unknown"})
        attributeName!: string

        keyUp(event) {
            this.$emit("keyup", {attributeName: this.attributeName, value: event.target.value})
        }
    }
</script>

<style scoped lang="scss">
  @import "@/gonzo_style.scss";
</style>