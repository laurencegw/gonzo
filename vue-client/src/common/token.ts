export const TOKEN_KEY = "userToken"

export function getToken(): Promise<String> {
    const token = localStorage.getItem(TOKEN_KEY)
    if (token === null) {
        return Promise.reject(new Error("Not logged in"))
    }
    return Promise.resolve(token)
}
