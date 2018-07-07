import axios from "axios"
import {LoginCredentials, Role, User, Users, UserUpdate} from "@/users/api"


export class UsersClient implements Users {
    private usersBasePath = "api/users"

    private headers(token: string): any {
        return {Authorization: `Bearer ${token}`}
    }

    assertValid(token: string): Promise<boolean> {
        throw Error("Not Implemented")
    }

    getUserFromToken(token: string): Promise<User> {
        return new Promise<User>((resolve, reject) => {
            axios.get<string>(
                `${this.usersBasePath}/me`,
                {
                    headers: this.headers(token),
                    responseType: "json"
                }
            ).then((response) => {
                    resolve(new User(response.data))
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
    private idCounter = 0
    private users: Map<number, User> = new Map()
    private userPasswords: Map<number, string> = new Map()

    constructor() {
        const id = this.idCounter++
        const adminUser = new User({
            id: id,
            email: "admin@email.com",
            handle: "admin1",
            role: Role.AUTHOR,
            firstName: "Sarah",
            lastName: "Smith"
        })
        const adminUserPassword = "password"
        this.users.set(id, adminUser)
        this.userPasswords.set(id, adminUserPassword)
    }

    assertValid(token: string): Promise<boolean> {
        return new Promise<boolean>(
            (resolve) => {
                resolve(true)
            }
        )
    }

    getUserFromToken(token: string): Promise<User> {
        return new Promise<User>((resolve, reject) => {
            const userID = Number(token)
            if (userID !== null) {
                resolve(this.users.get(userID!))
            }
        })
    }

    login(credentials: LoginCredentials): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            for (const user of this.users.values()) {
                if (user.email === credentials.email) {
                    const userPassword = this.userPasswords.get(user.id)
                    const passwordMatched = userPassword === credentials.password
                    if (passwordMatched) {
                        resolve(`${user.id}`)
                        return
                    }
                }
            }
            reject(Error("No"))
        })
    }

    updateUser(token: string, user: UserUpdate): Promise<User> {
        throw Error("Not Implemented")
    }
}
