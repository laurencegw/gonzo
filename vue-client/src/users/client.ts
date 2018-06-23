import axios from "axios"
import {LoginCredentials, User, Users, UserUpdate} from "./api"


export class UsersClient implements Users {
    private usersBasePath = "api/users"

    assertLoggedIn(token: string): Promise<boolean> {
        throw Error("Not Implemented")
    }

    getUserFromToken(token: string): Promise<User> {
        const header = `Authorization: Bearer ${token}`
        return new Promise<User>((resolve, reject) => {
            axios.get<string>("/api/me", { headers: { Authorization: `Bearer ${token}` }, responseType: "json" }).then((response) => {
                    // @ts-ignore
                resolve(response.data as User)
                }
            ).catch((rejection) => {
                reject(rejection)
            })
        })
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
