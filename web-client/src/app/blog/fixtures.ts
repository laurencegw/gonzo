import {BlogEntryHeader} from "./blog.models";
import {Component} from "@angular/core";

/**
 * Examples:
 * MockComponent({ selector: 'cranium' });
 * MockComponent({ selector: 'arm', inputs: ['side'] });
 */
export function MockComponent(options: Component): Component {
  let metadata: Component = {
    selector: options.selector,
    template: options.template || '',
    inputs: options.inputs,
    outputs: options.outputs
  };
  return Component(metadata)(class _ {});
}


export function blogEntryHeader(): BlogEntryHeader {
  return new BlogEntryHeader(1, new Date(), new Date(), new Date(), "Blog Title")
}
