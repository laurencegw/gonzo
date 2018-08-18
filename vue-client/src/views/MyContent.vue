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
                            <header-list :article-headers="articleHeaders" @selected="selected"></header-list>
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
                            <v-button v-if="viewing && unpublishedChanges" @click="publishHandler">Publish</v-button>
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
    import HeaderList from "@/articles/HeaderList.vue"
    import {ArticleDraft, ArticleDraftNew, ArticleHeader} from "../articles/api"
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
        selectedArticleHeader?: ArticleHeader

        @Getter user!: User
        @Getter articleHeaders!: Array<ArticleHeader>
        @Getter articleDraft!: ArticleDraft

        @Action loadDraftHeadersForAuthor
        @Action deleteArticle
        @Action createArticle
        @Action publishArticle

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

        get unpublishedChanges() {
            return this.articleDraft ? this.articleDraft.unpublishedChanges : false
        }

        selected(articleHeader: ArticleHeader) {
            this.selectedArticleHeader = articleHeader
            this.$router.push({name: "draft", params: {id: `${articleHeader.id}`}})
        }

        newHandler() {
            const userID = this.$store.getters.user.id
            const now = nowString()
            this.createArticle(new ArticleDraftNew(
                `New Article ${now}`,
                "",
                userID
            )).then(articleDraft => {
                this.$router.push({name: "edit", params: {id: articleDraft.id}})
            })
        }

        deleteHandler() {
            this.deleteArticle().then(
                () => {
                    this.$router.push({name: "my-content"})
                }
            )
        }

        publishHandler() {
            this.publishArticle()
        }
    }
</script>

<style lang="scss" src="@/gonzo_style.scss">

    .max-height-75 {
        height: 200px !important;
    }

</style>