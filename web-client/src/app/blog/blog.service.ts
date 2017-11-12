import {Observable} from "rxjs/Observable";
import {BlogEntry, BlogEntryHeader, copyBlogEntry} from "./blog.models";
import "rxjs/add/observable/of";

export interface BlogService {

  getBlogEntryHeaders(): Observable<BlogEntryHeader[]>

  getBlogEntryById(id: number): Observable<BlogEntry>

  createBlogEntry(newBlogEntry: BlogEntry): Observable<BlogEntry>

}


export class StubBlogService implements BlogService {

  private idCounter = 0;
  private blogEntries: BlogEntry[] = [];

  constructor() {
    for (let i = 0; i < 3; i++) {
      this.idCounter++;
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

  private nextID(): number {
    this.idCounter++
    return this.idCounter
  }

  createBlogEntry(newBlogEntry: BlogEntry): Observable<BlogEntry> {
    var created = copyBlogEntry(newBlogEntry)
    created.id = this.nextID()
    this.blogEntries.push(created)
    return Observable.of(copyBlogEntry(created))
  }


  getBlogEntryHeaders(): Observable<BlogEntryHeader[]> {
    return Observable.of(this.blogEntries.map((entry) => {
      return entry.toHeader()
    }))
  }

  getBlogEntryById(id: number): Observable<BlogEntry> {
    for (let entry of this.blogEntries) {
      if (entry.id == id) {
        return Observable.of(entry)
      }
    }
    return Observable.of(null)
  }

}
