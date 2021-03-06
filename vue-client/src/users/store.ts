import {LoginCredentials, User, Users} from "@/users/api"
import {ActionContext, ActionTree, GetterTree, MutationTree} from "vuex"
import {cloneDeep} from "lodash"
import Vue from "vue"
import {UsersClient} from "@/users/client"
import {isDev} from "@/utils"
import {TOKEN_KEY} from "@/common/token"

export enum LoginState {
    UNKNOWN,
    LOGGED_IN,
    NOT_LOGGED_IN,
    TRYING_TO_LOGIN,
    FAILED,
}

class UserState {
    user?: User
    loginState = LoginState.UNKNOWN
    loginErrorMessage = ""
}

const getters: GetterTree<UserState, any> = {
    user(state: UserState): User | undefined {
        return cloneDeep(state.user)
    },
    loginState(state: UserState): LoginState {
        return state.loginState
    },
    loginErrorMessage(state: UserState): string {
        return state.loginErrorMessage
    }
}

const mutations: MutationTree<UserState> = {
    setLoggedInUser(userState: UserState, user: User) {
        Vue.set(userState, "user", user)
        Vue.set(userState, "loginErrorMessage", "")
        Vue.set(userState, "loginState", LoginState.LOGGED_IN)
    },
    setLoginFailed(userState: UserState, message: String) {
        Vue.set(userState, "loginErrorMessage", message)
        Vue.set(userState, "loginState", LoginState.FAILED)
    },
    clearErrors(userState: UserState) {
        Vue.set(userState, "loginErrorMessage", "")
    },
    setLoggingIn(userState: UserState) {
        Vue.set(userState, "loginErrorMessage", "")
        Vue.set(userState, "loginState", LoginState.TRYING_TO_LOGIN)
    },
    setLoggedOut(userState: UserState) {
        Vue.set(userState, "loginErrorMessage", "")
        Vue.set(userState, "loginState", LoginState.NOT_LOGGED_IN)
    }
}

const buildActions = function (userClient: Users): ActionTree<UserState, any> {
    const actions: ActionTree<UserState, any> = {
        login(store: ActionContext<UserState, any>, loginCredentials: LoginCredentials) {
            store.commit("setLoggingIn")
            const loginErrorHandler = function (reason) {
                store.commit("setLoginFailed", "Sorry that did not work. Try again.")
            }
            userClient.login(loginCredentials).then((token) => {
                localStorage.setItem(TOKEN_KEY, token)
                userClient.getUserFromToken(token).then((user) => {
                    store.commit("setLoggedInUser", user)
                }).catch((reason) => {
                    loginErrorHandler(reason)
                })
            }).catch((reason) => {
                loginErrorHandler(reason)
            })
        },
        checkLoginState(store: ActionContext<UserState, any>) {
            const token = localStorage.getItem(TOKEN_KEY)
            if (token === null) {
                store.commit("setLoggedOut")
                return Promise.resolve()
            } else {
                return userClient.getUserFromToken(token).then((user) => {
                    store.commit("setLoggedInUser", user)
                }).catch((reason) => {
                    localStorage.removeItem(TOKEN_KEY)
                    store.commit("setLoggedOut")
                })
            }
        },
        logout(store: ActionContext<UserState, any>) {
            localStorage.removeItem(TOKEN_KEY)
            store.commit("setLoggedOut")
            return Promise.resolve(null)
        }
    }
    return actions
}


/**
 * Constructor function that allows injection of the user client.
 * @param userClient
 */
const createStore = function (userClient: Users): any {
    return {
        state: new UserState(),
        getters: getters,
        mutations: mutations,
        actions: buildActions(userClient)
    }
}

const client = function (): Users {
    if (isDev()) {
        // return new UsersClientFake()
        return new UsersClient()
    }
    return new UsersClient()
}

export const UserStore = createStore(client())
