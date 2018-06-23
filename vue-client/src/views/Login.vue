<template>
  <b-row>
    <b-col md="6" offset-md="3">
      <div class="bordered">
        <br>
        <b-row>
          <b-col md="3" offset-md="1">
            <label>email</label>
          </b-col>
          <b-col md="3">
            <input type="text" v-model="email">
          </b-col>
        </b-row>
        <br>
        <b-row>
          <b-col md="3" offset-md="1">
            <label>password</label>
          </b-col>
          <b-col md="3">
            <input type="password" v-model="password">
          </b-col>
        </b-row>
        <br>
        <b-row>
          <b-col md="2" offset-md="5">
            <button @click="login">
              Login
            </button>
          </b-col>
        </b-row>
        <br>
      </div>
    </b-col>
  </b-row>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import {Mutation} from "vuex-class"
    import {UsersClient} from "../users/client"
    import {LoginCredentials} from "../users/api"

    @Component
    export default class LoginComponent extends Vue {
        client: UsersClient = new UsersClient()

        email = ""
        password = ""

        @Mutation setUser

        login() {
            this.client.login(new LoginCredentials(
                this.email,
                this.password
            )).then((token) => {
                this.client.getUserFromToken(token).then((user) => {
                    this.setUser(user)
                    this.$router.push({name: "frontpage"})
                })
            })
        }


    }


</script>
