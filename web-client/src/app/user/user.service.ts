import {Observable} from "rxjs/Observable"

export interface Users {

  /**
   * Sign in with credentials.
   *
   * @param email
   * @param password
   */
  signIn(email: string, password: string): Observable<User>

  getToken(): string

  getUser(): User
}

class NotSignedInError extends Error {
}

class User {
  id: number
  email: string
  handle: string
  role: string
  firstName: string
  lastName: string

  constructor(id: number, email: string, handle: string, role: string, firstName: string, lastName: string) {
    this.id = id
    this.email = email
    this.handle = handle
    this.role = role
    this.firstName = firstName
    this.lastName = lastName
  }
}
