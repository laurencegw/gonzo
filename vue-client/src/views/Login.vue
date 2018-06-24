import {LoginState} from "../users/store";
import {LoginState} from "../users/store";
<template>
  <b-row>
    <b-col md="8" offset-md="2">
      <div class="bordered">

        <div v-if="loginFailed">
          <br>
          <b-row>
            <b-col md="5" offset-md="1">
              <div class="failure-message no-padding">
                {{ loginErrorMessage }}
              </div>
            </b-col>
          </b-row>
        </div>
        <br>
        <b-row>
          <b-col md="2" offset-md="1">
            <label>email</label>
          </b-col>
          <b-col md="3" offset-md="1">
            <input type="text" v-model="email">
          </b-col>
        </b-row>
        <br>
        <b-row>
          <b-col md="2" offset-md="1">
            <label>password</label>
          </b-col>
          <b-col md="3" offset-md="1">
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
    import {Component, Vue, Watch} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import {UsersClientFake} from "@/users/client"
    import {LoginCredentials, Users} from "@/users/api"
    import {LoginState} from "@/users/store"

    @Component
    export default class LoginComponent extends Vue {
        client: Users = new UsersClientFake()

        email = ""
        password = ""

        @Getter loginState?: LoginState
        @Getter loginErrorMessage?: string

        @Action("login") loginAction

        login() {
            this.loginAction(new LoginCredentials(
                this.email,
                this.password
            ))
        }

        @Watch("loginState")
        onLoginStateChange(newState: LoginState, oldState: LoginState) {
            if (newState === LoginState.LOGGED_IN) {
                this.$router.push({name: "frontpage"})
            }
        }

        get loginFailed(): Boolean {
            return this.loginState === LoginState.FAILED
        }

    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>
