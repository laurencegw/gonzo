<template>
    <div>
        <v-hunter-quote>Yesterday's weirdness is tomorrow's reason why.</v-hunter-quote>
    </div>
</template>
<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import VHunterQuote from "@/components/VHunterQuote.vue"

    @Component({
        components: {VHunterQuote}
    })
    export default class Coders extends Vue {
    }
</script>