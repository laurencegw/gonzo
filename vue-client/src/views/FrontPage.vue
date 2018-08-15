<template>
    <div>
        <v-hunter-quote>So we shall let the reader answer this question for himself: who is the happier man, he who has
            braved the storm of life and lived or he who has stayed securely on shore and merely existed?
        </v-hunter-quote>
    </div>
</template>
<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import VHunterQuote from "@/components/VHunterQuote.vue"

    @Component({
        components: {VHunterQuote}
    })
    export default class FrontPage extends Vue {
    }
</script>