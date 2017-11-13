export class NewBlogEntry {
  title: String;
  content: String;
  publish: boolean;


  constructor(title: String, content: String, publish: boolean) {
    this.title = title;
    this.content = content;
    this.publish = publish;
  }
}


export class BlogEntry {
  id: number;
  firstPublished: Date;
  created: Date;
  updated: Date;
  title: String;
  content: String;

  constructor(id?: number, firstPublished?: Date, created?: Date, updated?: Date, title?: String, content?: String) {
    this.id = id;
    this.firstPublished = firstPublished;
    this.created = created;
    this.updated = updated;
    this.title = title;
    this.content = content;
  }

  toHeader(): BlogEntryHeader {
    return new BlogEntryHeader(
      this.id,
      this.firstPublished,
      this.created,
      this.updated,
      this.title
    )
  }

}

export function copyBlogEntry(blogEntry: BlogEntry): BlogEntry {
  return new BlogEntry(
    blogEntry.id,
    blogEntry.firstPublished,
    blogEntry.created,
    blogEntry.updated,
    blogEntry.title,
    blogEntry.content
  )
}


/**
 * A snapshot summary of a BlogEntry
 */
export class BlogEntryHeader {
  id: number;
  firstPublished: Date;
  created: Date;
  updated: Date;
  title: String;


  constructor(id: number, firstPublished?: Date, created?: Date, updated?: Date, title?: String) {
    this.id = id;
    this.firstPublished = firstPublished;
    this.created = created;
    this.updated = updated;
    this.title = title;
  }
}
