import {Observable} from "rxjs/Observable"
import {BlogEntry, BlogEntryHeader, copyBlogEntry, NewBlogEntry} from "./blog.models"
import "rxjs/add/observable/of"

export interface BlogService {

  getBlogEntryHeaders(): Observable<BlogEntryHeader[]>

  getBlogEntryById(id: number): Observable<BlogEntry>

  createBlogEntry(newBlogEntry: NewBlogEntry): Observable<BlogEntry>

}


export class StubBlogService implements BlogService {

  blogEntries: BlogEntry[] = []
  private idCounter = 0

  constructor() {
    for (let i = 0; i < 3; i++) {
      this.idCounter++
      this.blogEntries.push(
        new BlogEntry(
          this.idCounter,
          new Date(),
          new Date(),
          new Date(),
          "My Blog Entry " + this.idCounter,
          "A bunch of text that gets shown as a blog entry. " +
          "A bunch of text that gets shown as a blog entry. " +
          "A bunch of text that gets shown as a blog entry. " +
          "A bunch of text that gets shown as a blog entry. " +
          "A bunch of text that gets shown as a blog entry. " +
          "A bunch of text that gets shown as a blog entry. " +
          "A bunch of text that gets shown as a blog entry. " +
          "A bunch of text that gets shown as a blog entry. "
        )
      )
    }
  }

  createBlogEntry(newBlogEntry: NewBlogEntry): Observable<BlogEntry> {
    const created = new BlogEntry(
      this.nextID(),
      newBlogEntry.publish ? new Date() : null,
      new Date(),
      new Date(),
      newBlogEntry.title,
      newBlogEntry.content
    )
    this.blogEntries.push(created)
    return Observable.of(copyBlogEntry(created))
  }

  getBlogEntryHeaders(): Observable<BlogEntryHeader[]> {
    return Observable.of(this.blogEntries.map((entry) => {
      return entry.toHeader()
    }))
  }

  getBlogEntryById(id: number): Observable<BlogEntry> {
    for (const entry of this.blogEntries) {
      if (entry.id === id) {
        return Observable.of(entry)
      }
    }
    return Observable.of(null)
  }

  private nextID(): number {
    this.idCounter++
    return this.idCounter
  }

}
