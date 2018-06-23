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
    import {Getter} from "vuex-class"

    @Component
    export default class App extends Vue {
        @Getter user

        get isLoggedIn(): Boolean {
            return this.user !== undefined
        }
    }
</script>

<style lang="scss">

  $color-dark-dark: #323031;
  $color-medium-dark: #4e4b4d;
  $color-light-dark: #747570;
  $color-border-dark: #1a1a1a;
  $color-white: #e6ffff;
  $color-turquoise: #69FFF1;
  $color-green: #63D471;
  $color-darker-green: #63A46C;

  .title-bar {
    background-color: $color-dark-dark;
    color: azure;
    font-size: 25px;
    text-align: center;
  }

  .nav-item {
    background-color: $color-medium-dark;
    white-space: nowrap;
    font-size: 20px;
    color: $color-white;
    cursor: pointer;
    border-style: solid;
    border-color: $color-turquoise;
    border-left: 0px;
    border-right: 0px;
    border-bottom: 0px;
  }

  .nav-item:hover{
    background: $color-light-dark;
  }

  .no-padding {
    padding: 0 !important;
  }

  .nav-buffer {
    background-color: $color-medium-dark;
    padding: 0px !important;
    border-style: solid;
    border-color: $color-turquoise;
    border-left: 0px;
    border-right: 0px;
    border-bottom: 0px;
  }

  .main-body {
    background-color: $color-dark-dark;
    color: $color-white;
  }

  .text-center {
    text-align: center;
  }

  .router-link-active.nav-item {
    background-color: $color-dark-dark;
    color: $color-turquoise;
    border-style: solid;
    border-color: $color-turquoise;
    border-top: 0px;

  }

  .bordered {
    border-style: solid;
    border-color: $color-turquoise;
    border-radius: 10px;
    border-width: 2px;
  }

  .bordered-thin{
    border-style: solid;
    border-color: $color-turquoise;
    border-radius: 10px;
    border-width: 1px;
  }

  .margined {
    margin: 2px;
  }

  .button-medium {
    border-style: solid;
    border-color: $color-turquoise;
    border-radius: 5px;
    border-width: 1px;
    font-size: 15px;
    cursor: pointer;
  }

  .button-medium:hover {
    background: $color-medium-dark;
  }
</style>

