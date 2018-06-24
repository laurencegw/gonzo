<template>
  <div>
    <h1>@{{user.handle}}</h1>
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
