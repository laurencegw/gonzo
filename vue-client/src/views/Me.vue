<template>
    <div>
        <h1>This is My Page</h1>
        <p>You are {{user}}</p>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import {Getter} from "vuex-class"


    @Component
    export default class MeComponent extends Vue {
        @Getter user
    }


</script>
