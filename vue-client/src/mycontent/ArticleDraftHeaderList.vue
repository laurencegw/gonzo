<template>
  <div>
    <b-row v-for="(header, index) in articleHeaders">
      <b-col :class="myStyle(index)">
        <div @click="select(index)">
          <div>{{header.title}}</div>
          <div>Created: {{ header.created | fDate }}</div>
          <div>Updated: {{header.updated | fDate }}</div>
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import {Prop} from "vue-property-decorator"
    import VButton from "../components/VButton.vue"
    import {ArticleHeader} from "../articles/api"

    @Component({
        components: {
            VButton
        }
    })
    export default class EntryList extends Vue {
        selected = -1
        @Prop() articleHeaders!: Array<ArticleHeader>

        select(index: number) {
            this.selected = index
            this.$emit("selected", this.articleHeaders[index])
        }

        myStyle(index: number): Array<String> {
            if (this.selected === index) {
                return ["margined", "bordered-neon", "background-medium-dark"]
            } else {
                return ["margined", "bordered-light-thin"]
            }
        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>