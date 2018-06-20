<template>
    <div>
        <label for="email-input">email</label>
        <input v-model="email" id="email-input" type="text">
        <label for="password-input">password</label>
        <input v-model="password" id="password-input" type="password">
        <button @click="login">Login</button>

        <label>{{ token }}</label>
    </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator"
import {UsersClient} from "@/users/client"
import {LoginCredentials} from "@/users/api"

@Component
export default class Login extends Vue {
    client: UsersClient = new UsersClient()

    email = ""
    password = ""
    token = ""

    login() {
        this.client.login(new LoginCredentials(
            this.email,
            this.password
        )).then( (token) => {
            this.token = token
        })
    }

}
</script>

<style scoped>

</style>