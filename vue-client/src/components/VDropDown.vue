<template>
  <div>
    <div class="header" @click="dropDownClicked">{{header}}</div>
    <div v-if="showDropDown" class="menu">
      <div class="item" v-for="(name, index) in dropDownNames">
        <div @click="itemClicked(index)">{{name}}</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator"

    @Component({
        name: "v-dropdown"
    })
    export default class VDropDown extends Vue {
        showDropDown: Boolean = false

        @Prop() header!: string

        @Prop() dropDownNames!: string[]

        @Prop() dropDownHandlers!: (() => void)[]


        dropDownClicked() {
            console.log("dropdown clicked")
            this.showDropDown = !this.showDropDown
        }

        itemClicked(index: number) {
            this.showDropDown = false
            this.dropDownHandlers[index]()
        }
    }
</script>

<style scoped lang="scss">
  @import "@/gonzo_style.scss";

  .header {
    cursor: pointer;
  }

  .menu {
    position: absolute;
    z-index: 1;
    display: block;
  }

  .item {
    background: $color-dark-dark;
    border-style: solid;
    border-color: $color-turquoise;
    border-width: 1px;
    margin: 0px;
    padding: 0px;
    cursor: pointer;
  }

</style>