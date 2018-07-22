<template>
  <div>
    <b-row>
      <b-col md="2">
        MY CONTENT
      </b-col>
      <b-col md="2">
        <v-button v-if="notModifying" @click="$router.push({name:'new-entry'})">New</v-button>
      </b-col>
    </b-row>
    <br>
    <div v-if="isLoading">Loading..</div>
    <div v-else>
      <b-row>
        <b-col>
          <header-list :blog-headers="blogHeaders"></header-list>
        </b-col>
        <b-col>
          <router-view/>
        </b-col>
      </b-row>
    </div>
  </div>
</template>
<script lang="ts">
    import {Component, Vue, Watch} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import VButton from "@/components/VButton.vue"
    import HeaderList from "@/blogs/HeaderList.vue"
    import {BlogHeader} from "../blogs/api"
    import {User} from "../users/api"

    const STATE_LOADING = "LOADING"
    const STATE_LOADED = "LOADED"

    @Component({
        components: {
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
    }
</script>