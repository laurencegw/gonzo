import {Component, Input, OnInit} from '@angular/core';
import {BlogEntry} from "../blog.models";

@Component({
  selector: 'app-blog-entry-view',
  templateUrl: './blog-entry-view.component.html',
  styleUrls: ['./blog-entry-view.component.css']
})
export class BlogEntryViewComponent implements OnInit {

  @Input() blogEntry: BlogEntry = new BlogEntry();

  constructor() {

  }

  ngOnInit() {
  }

}
