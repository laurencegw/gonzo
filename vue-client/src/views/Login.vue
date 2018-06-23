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
            <input type="text">
          </b-col>
        </b-row>
        <br>
        <b-row>
          <b-col md="3" offset-md="1">
            <label>password</label>
          </b-col>
          <b-col md="3">
            <input type="password">
          </b-col>
        </b-row>
        <br>
        <b-row>
          <b-col md="2" offset-md="5">
            <button>
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
    import Login from "../users/views/Login";

    @Component({
        components: {Login}
    })
    export default class LoginComponent extends Vue {
    }


</script>
