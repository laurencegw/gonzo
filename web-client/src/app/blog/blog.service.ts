import {Observable} from "rxjs/Observable";
import {BlogEntry, BlogEntryHeader} from "./blog.models";
import "rxjs/add/observable/of";

export interface BlogService {

  getBlogEntryHeaders(): Observable<BlogEntryHeader[]>

  getBlogEntryById(id: number): Observable<BlogEntry>

}


export class StubBlogService implements BlogService {

  private blogEntries: BlogEntry[] = [];

  constructor() {
    for (let i = 0; i < 100; i++) {
      this.blogEntries.push(
        new BlogEntry(
          i,
          new Date(),
          new Date(),
          "My Blog Entry "+i,
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry" +
          "A bunch of text that gets shown as a blog entry"
        )
      )
    }

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
