<template>
  <div>
    <b-row>
      <b-col md="10">
        My Content
      </b-col>
      <b-col md="2">
        <v-button v-if="notModifying" @click="$router.push({name:'new-entry'})">New</v-button>
      </b-col>
    </b-row>
    <br>
    <b-row>
      <router-view/>
    </b-row>
  </div>
</template>
<script lang="ts">
    import {Component, Vue, Watch} from "vue-property-decorator"
    import VButton from "@/components/VButton.vue"

    @Component({
        components: {
            VButton
        }
    })
    export default class MyContent extends Vue {

        notModifying = true

        @Watch("$route")
        checkIfOnLoginPage() {
            this.notModifying = this.$router.currentRoute.name !== "new-entry"
        }
    }
</script>