import {BlogEntryHeader} from "./blog.models";

export function blogEntryHeader(): BlogEntryHeader{
  return new BlogEntryHeader(1, new Date(), new Date(), "Blog Title")
}
