export class BlogEntry {
  id: number;
  created: Date;
  updated: Date;
  title: String;
  content: String;


  constructor(id: number, created: Date, updated: Date, title: String, content: String) {
    this.id = id;
    this.created = created;
    this.updated = updated;
    this.title = title;
    this.content = content;
  }

  toHeader(): BlogEntryHeader {
    return new BlogEntryHeader(
      this.id,
      this.created,
      this.updated,
      this.title
    )
  }

}

/**
 * A snapshot summary of a BlogEntry
 */
export class BlogEntryHeader {
  id: number;
  created: Date;
  updated: Date;
  title: String;


  constructor(id: number, created: Date, updated: Date, title: String) {
    this.id = id;
    this.created = created;
    this.updated = updated;
    this.title = title;
  }
}
