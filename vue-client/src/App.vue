import {LoginState} from "./users/store";
<template>
  <div id="app" class="main-body">
    <b-container fluid>
      <b-row class="title-bar" align-v="center">
        <b-col md="4" offset-md="4">
          &lt;GONZO CODE&gt;
        </b-col>
        <b-col md="1" offset-m="3">
          <div v-if="isLoggedIn">{{user.handle}}</div>
          <router-link v-else to="/login" tag="div">
            <div class="button-medium">login</div>
          </router-link>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="3" class="nav-buffer"></b-col>
        <b-col md="6">
          <b-row>
            <b-col md="4" class="text-center no-padding">
              <router-link to="/tags" tag="div" class="nav-item text-center">
                <div>Tags</div>
              </router-link>
            </b-col>
            <b-col md="4" class="text-center no-padding">
              <router-link to="/frontpage" tag="div" class="nav-item text-center">
                <div>Front Page</div>
              </router-link>
            </b-col>
            <b-col md="4" class="text-center no-padding">
              <router-link to="/coders" tag="div" class="nav-item text-center">
                <div>Coders</div>
              </router-link>
            </b-col>
          </b-row>
        </b-col>
        <b-col md="3" class="nav-buffer"></b-col>
      </b-row>
    </b-container>
    <br>
    <b-container>
      <b-row>
        <b-col md="8" offset-md="2">
          <router-view/>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import {Getter, Action} from "vuex-class"
    import {User} from "@/users/api"
    import {LoginState} from "@/users/store"

    @Component
    export default class App extends Vue {
        @Getter user?: User
        @Getter loginState?: LoginState

        @Action checkLoginState

        created() {
            this.checkLoginState()
        }

        get isLoggedIn(): Boolean {
            return this.loginState === LoginState.LOGGED_IN
        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>

