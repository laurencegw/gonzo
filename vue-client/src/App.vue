import {LoginState} from "./users/store";
<template>
  <div id="app" class="main-body">
    <b-container fluid>
      <b-row class="title-bar" align-v="center">
        <b-col md="4" offset-md="4">
          &lt;GONZO CODE&gt;
        </b-col>
        <b-col md="1" offset-m="3">
          <user-status></user-status>
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
        <b-col md="12">
          <router-view/>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"
    import UserStatus from "@/users/UserStatus.vue"

    @Component({
        components: {
            UserStatus
        }
    })
    export default class App extends Vue {

    }
</script>

<style lang="scss" src="@/gonzo_style.scss">
</style>

