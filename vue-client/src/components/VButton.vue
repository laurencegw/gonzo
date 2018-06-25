<template>
  <div @click="onClick" class="button-medium">
    <slot>Button</slot>
  </div>
</template>

<script lang="ts">
    import {Component, Emit, Vue} from "vue-property-decorator"

    @Component({
        name: "v-button"
    })
    export default class VButton extends Vue {
        @Emit("click")
        onClick() {}
    }
</script>

<style scoped lang="scss">
  @import "@/gonzo_style.scss";

  .button-medium {
    border-style: solid;
    border-color: $color-turquoise;
    border-radius: 5px;
    border-width: 1px;
    font-size: 15px;
    cursor: pointer;
    text-align: center;
  }

  .button-medium:hover {
    background: $color-medium-dark;
  }

  .button-medium:active{
    border-width: 3px;
  }

</style>