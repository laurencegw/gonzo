<template>
<div>
  EntryList
</div>
</template>

<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import VButton from "@/components/VButton.vue"

    @Component({
        components: {
            VButton
        }
    })
    export default class EntryList extends Vue {
    }
</script>

<style scoped>

</style>