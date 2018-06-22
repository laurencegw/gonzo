import {Component, Inject, OnInit} from '@angular/core';
import {BlogService} from "../blog.service";

@Component({
  selector: 'app-blog-list',
  templateUrl: './blog-list.component.html',
  styleUrls: ['./blog-list.component.css']
})
export class BlogListComponent implements OnInit {

  blogService: BlogService;

  constructor(@Inject('BlogService') blogService: BlogService) {
    this.blogService = blogService
  }

  ngOnInit() {
  }

}
