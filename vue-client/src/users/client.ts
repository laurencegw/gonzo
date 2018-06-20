import axios from "axios"
import {LoginCredentials, User, Users, UserUpdate} from "./api"


export class UsersClient implements Users {
    private usersBasePath = "api/users"

    assertLoggedIn(token: string): Promise<boolean> {
        throw Error("Not Implemented")
    }

    getUserFromToken(token: string): Promise<User> {
        throw Error("Not Implemented")
    }

    login(credentials: LoginCredentials): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            axios.post<string>("/api/login", credentials).then((response) => {
                    resolve(response.data)
                }
            ).catch((rejection) => {
                reject(rejection)
            })
        })
    }

    updateUser(user: UserUpdate): Promise<User> {
        throw Error("Not Implemented")
    }
}
