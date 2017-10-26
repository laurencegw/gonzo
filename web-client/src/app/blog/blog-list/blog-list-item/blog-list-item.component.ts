import {Component, Input, OnInit} from '@angular/core';
import {BlogEntryHeader} from "../../blog.models";

@Component({
  selector: 'app-blog-list-item',
  templateUrl: './blog-list-item.component.html',
  styleUrls: ['./blog-list-item.component.css']
})
export class BlogListItemComponent implements OnInit {

  @Input() blogEntryHeader: BlogEntryHeader;

  constructor() {
  }

  ngOnInit() {
  }

}
