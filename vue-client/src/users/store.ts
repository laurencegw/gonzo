import {User} from "@/users/api"
import {GetterTree, MutationTree} from "vuex"

class UserState {
    user?: User
}

const getters: GetterTree<UserState, User> = {
    user(state: UserState): User | undefined {
        return state.user
    }
}

const mutations: MutationTree<UserState> = {
    setUser(userState: UserState, user: User) {
        userState.user = user
    }
}

export const UserStore = {
    state: new UserState(),
    getters: getters,
    mutations: mutations
}
