<template>
  <b-container fluid>
    <b-row>
      <b-col md="1"><label for="title-input">Title:</label></b-col>
      <b-col md="9"><input id="title-input" type="text"></b-col>
      <b-col md="2"><v-button @click="save()">Save</v-button></b-col>
    </b-row>
    <br>
    <b-row><textarea rows="15" cols="80"></textarea></b-row>
  </b-container>

</template>
<script lang="ts">
    import Vue from "vue"
    import Component from "vue-class-component"
    import VButton from "@/components/VButton.vue"

    @Component({
        components: {
            VButton
        }
    })
    export default class NewEntry extends Vue {


        save(){

        }

    }
</script>

<style scoped>

</style>