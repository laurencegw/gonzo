import {User} from "@/users/api"
import {GetterTree} from "vuex"

class UserState {
    user?: User
}

const getters: GetterTree<UserState, User> = {
    user(state: UserState): User | undefined {
        return state.user
    }
}

export const UserStore = {
    state: new UserState(),
    getters: getters
}
