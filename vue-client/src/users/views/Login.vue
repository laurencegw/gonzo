<template>
    <div>
        <label for="email-input">email</label>
        <input id="email-input" type="text">
        <label for="password-input">password</label>
        <input id="password-input" type="password">
    </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator"

@Component
export default class Login extends Vue {

}
</script>

<style scoped>

</style>