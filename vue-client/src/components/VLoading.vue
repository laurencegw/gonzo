<template>
  <div v-if="isLoading">Loading..</div>
  <div v-else>
    <slot></slot>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator"

    @Component({
        name: "v-loading"
    })
    export default class VButton extends Vue {
        @Prop() isLoading!: Boolean
    }
</script>