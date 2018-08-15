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
                            <v-button v-if="notModifying" @click="newHandler">New</v-button>
                            <v-button v-if="viewing" @click="$router.push({name:'edit'})">Edit</v-button>
                            <v-button v-if="viewing" @click="deleteHandler">Delete</v-button>
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
    import {BlogDraftNew, BlogHeader} from "../blogs/api"
    import {User} from "@/users/api"
    import VLoading from "@/components/VLoading.vue"
    import {nowString} from "@/common/time"


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
        viewing = false
        state = STATE_LOADING
        selectedBlogHeader?: BlogHeader

        @Getter user!: User
        @Getter blogHeaders!: Array<BlogHeader>

        @Action loadDraftHeadersForAuthor
        @Action deleteBlog
        @Action createBlog

        mounted() {
            this.checkCurrentActivity()
            this.loadDraftHeadersForAuthor(this.user.id).then(() => {
                this.state = STATE_LOADED
            })
        }

        @Watch("$route")
        checkCurrentActivity() {
            this.notModifying = !["new-entry", "edit"].includes(this.$router.currentRoute.name as string)
            this.viewing = this.$router.currentRoute.name === "draft"
        }

        get isLoading() {
            return this.state !== STATE_LOADED
        }

        selected(blogHeader: BlogHeader) {
            this.selectedBlogHeader = blogHeader
            this.$router.push({name: "draft", params: {id: `${blogHeader.id}`}})
        }

        newHandler() {
            const userID = this.$store.getters.user.id
            const now = nowString()
            this.createBlog(new BlogDraftNew(
                `New Blog ${now}`,
                "",
                userID
            )).then(blogDraft => {
                this.$router.push({name: "edit", params: {id: blogDraft.id}})
            })
        }

        deleteHandler() {
            this.deleteBlog().then(
                () => {
                    this.$router.push({name: "my-content"})
                }
            )
        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">

    .max-height-75 {
        height: 200px !important;
    }

</style>