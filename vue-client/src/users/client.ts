import {LoginCredentials, User, UserUpdate, Users} from "./api"


class Client implements Users {
    private usersBasePath = "api/users"

    assertLoggedIn(token: string): Promise<boolean> {
        throw Error("Not Implemented")
    }

    getUserFromToken(token: string): Promise<User> {
        throw Error("Not Implemented")
    }

    login(credentials: LoginCredentials): Promise<string> {
        throw Error("Not Implemented")
    }

    updateUser(user: UserUpdate): Promise<User> {
        throw Error("Not Implemented")
    }
}
