export enum Role {
    AUTHOR,
    ADMIN,
    READER
}

export class LoginCredentials {
    email: string
    password: string

    constructor(email: string, password: string) {
        this.email = email
        this.password = password
    }
}

export class User {
    public id: number
    public email: string
    public handle: string
    public role: Role
    public firstName: string
    public lastName: string

    constructor(id: number, email: string, handle: string, role: Role, firstName: string, lastName: string) {
        this.id = id
        this.email = email
        this.handle = handle
        this.role = role
        this.firstName = firstName
        this.lastName = lastName
    }
}

export class UserPublicHeader {
    id: number
    handle: string
    firstName: string
    lastName: string


    constructor(id: number, handle: string, firstName: string, lastName: string) {
        this.id = id
        this.handle = handle
        this.firstName = firstName
        this.lastName = lastName
    }
}

export class UserUpdate {
    id: number
    email: string
    firstName: string
    lastName: string


    constructor(id: number, email: string, firstName: string, lastName: string) {
        this.id = id
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
    }
}


export interface Users {
    updateUser(token: string, user: UserUpdate): Promise<User>

    login(credentials: LoginCredentials): Promise<string>

    assertValid(token: string): Promise<boolean>

    getUserFromToken(token: string): Promise<User>
}











