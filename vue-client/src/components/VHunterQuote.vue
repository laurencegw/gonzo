<template>
    <div>
        <p><q><i>
            <slot>We can't stop here, this is bat country!</slot>
        </i></q>
        </p>
        <p>- Hunter S Thompson</p>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"

    @Component({
        name: "v-hunter-quote"
    })
    export default class VHunterQuote extends Vue {
    }
</script>
