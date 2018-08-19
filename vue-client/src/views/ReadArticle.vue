<template>
    <v-hunter-quote>Life has become immeasurably better since I have been forced to stop taking it seriously.
    </v-hunter-quote>
</template>
<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import VHunterQuote from "@/components/VHunterQuote.vue"

    @Component({
        components: {VHunterQuote}
    })
    export default class ReadArticle extends Vue {
    }
</script>