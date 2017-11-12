export class BlogEntry {
  id: number;
  published: Date;
  created: Date;
  updated: Date;
  title: String;
  content: String;

  constructor(id?: number, published?: Date, created?: Date, updated?: Date, title?: String, content?: String) {
    this.id = id;
    this.published = published;
    this.created = created;
    this.updated = updated;
    this.title = title;
    this.content = content;
  }

  toHeader(): BlogEntryHeader {
    return new BlogEntryHeader(
      this.id,
      this.published,
      this.created,
      this.updated,
      this.title
    )
  }

}

export function copyBlogEntry(blogEntry: BlogEntry): BlogEntry {
  return new BlogEntry(
    blogEntry.id,
    blogEntry.published,
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
  published: Date;
  created: Date;
  updated: Date;
  title: String;


  constructor(id: number, published?: Date, created?: Date, updated?: Date, title?: String) {
    this.id = id;
    this.published = published;
    this.created = created;
    this.updated = updated;
    this.title = title;
  }
}
