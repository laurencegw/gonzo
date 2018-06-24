import axios from "axios"
import {LoginCredentials, User, Users, UserUpdate} from "./api"
import {Role} from "@/users/api"


export class UsersClient implements Users {
    private usersBasePath = "api/users"

    private headers(token: string): any {
        return {Authorization: `Bearer ${token}`}
    }

    assertValid(token: string): Promise<boolean> {
        throw Error("Not Implemented")
    }

    getUserFromToken(token: string): Promise<User> {
        const header = `Authorization: Bearer ${token}`
        return new Promise<User>((resolve, reject) => {
            axios.get<string>(
                `${this.usersBasePath}/me`,
                {
                    headers: this.headers(token),
                    responseType: "json"
                }
            ).then((response) => {
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

    updateUser(token: string, user: UserUpdate): Promise<User> {
        throw Error("Not Implemented")
    }
}


export class UsersClientFake implements Users {

    assertValid(token: string): Promise<boolean> {
        return new Promise<boolean>(
            (resolve) => {
                resolve(true)
            }
        )
    }

    getUserFromToken(token: string): Promise<User> {
        const header = `Authorization: Bearer ${token}`
        return new Promise<User>((resolve, reject) => {
            resolve(new User(
                1,
                "fake@email.com",
                "fakeDude",
                Role.AUTHOR,
                "Fakey",
                "Fakenson"
            ))
        })
    }

    login(credentials: LoginCredentials): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            resolve("arbitrary_credentials")
        })
    }

    updateUser(token: string, user: UserUpdate): Promise<User> {
        throw Error("Not Implemented")
    }
}
