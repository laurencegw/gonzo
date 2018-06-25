<template>
  <div>
    <v-drop-down
        v-if="isLoggedIn"
        :header="'@'+user.handle"
        :dropDownNames="dropDownNames()"
        :dropDownHandlers="dropDownHandlers()"
    >{{user.handle}}
    </v-drop-down>
    <v-button v-else-if="notOnLoginPage" @click="onLoginClick">login</v-button>
  </div>
</template>

<script lang="ts">
    import {Component, Vue, Watch} from "vue-property-decorator"
    import {Action, Getter} from "vuex-class"
    import {User} from "@/users/api"
    import {LoginState} from "@/users/store"
    import VButton from "@/components/VButton.vue"
    import VDropDown from "@/components/VDropDown.vue"

    @Component({
        name: "user-status",
        components: {VButton, VDropDown}
    })
    export default class UserStatus extends Vue {
        @Getter user?: User
        @Getter loginState?: LoginState

        @Action checkLoginState
        @Action logout

        notOnLoginPage = true

        mounted() {
            this.checkLoginState()
            this.checkIfOnLoginPage()
        }

        @Watch("$route")
        checkIfOnLoginPage() {
            this.notOnLoginPage = this.$router.currentRoute.path !== "/login"
        }

        get isLoggedIn(): Boolean {
            return this.loginState === LoginState.LOGGED_IN
        }

        onLoginClick() {
            this.$router.push({name: "login"})
        }

        dropDownNames(): string[] {
            return [
                "logout",
                "my content"
            ]
        }

        dropDownHandlers(): (() => void)[] {
            return [
                () => {
                    this.logout()
                },
                () => {
                    this.$router.push({name: "mycontent"})
                }
            ]
        }


    }
</script>

<style scoped>

</style>