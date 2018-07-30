<template>
  <div>
    <b-row>
      <b-col md="3">
        <b-row>
          <b-col>
            MY CONTENT
          </b-col>
        </b-row>
        <br>
        <v-loading :is-loading="isLoading">
          <b-row class="vertical-scroll max-height-75">
            <b-col>
              <header-list :blog-headers="blogHeaders" @selected="selected"></header-list>
            </b-col>
          </b-row>
        </v-loading>
        <b-row>
          <b-col><label></label></b-col>
        </b-row>
      </b-col>
      <b-col md="9">
        <b-row>
          <b-col>
            <b-row>
              <v-button v-if="notModifying" @click="$router.push({name:'new-entry'})">New</v-button>
            </b-row>
          </b-col>
        </b-row>
        <br>
        <b-row>
          <b-col>
            <router-view/>
          </b-col>
        </b-row>
      </b-col>
    </b-row>
  </div>
</template>
<script lang="ts">
    import {Component, Vue, Watch} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import VButton from "@/components/VButton.vue"
    import HeaderList from "@/blogs/HeaderList.vue"
    import {BlogHeader} from "../blogs/api"
    import {User} from "@/users/api"
    import VLoading from "@/components/VLoading.vue"

    const STATE_LOADING = "LOADING"
    const STATE_LOADED = "LOADED"

    @Component({
        components: {
            VLoading,
            VButton,
            HeaderList,
        }
    })
    export default class MyContent extends Vue {

        notModifying = true
        state = STATE_LOADING

        @Getter user!: User
        @Getter blogHeaders!: Array<BlogHeader>
        @Action loadDraftHeadersForAuthor

        mounted() {
            this.checkIfCurrentlyModifying()
            this.loadDraftHeadersForAuthor(this.user.id).then(() => {
                this.state = STATE_LOADED
            })
        }

        @Watch("$route")
        checkIfCurrentlyModifying() {
            this.notModifying = this.$router.currentRoute.name !== "new-entry"
        }

        get isLoading() {
            return this.state !== STATE_LOADED
        }

        selected(blogHeader: BlogHeader) {
            this.$router.push({name: "draft", params: {id: `${blogHeader.id}`}})
        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">

  .max-height-75 {
    height: 200px !important;
  }

</style>