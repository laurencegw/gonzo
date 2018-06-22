import {Component, Inject, OnInit} from "@angular/core"
import {BlogService} from "./blog.service"

@Component({
  selector: "app-blog",
  templateUrl: "./blog.component.html",
  styleUrls: ["./blog.component.css"]
})
export class BlogComponent implements OnInit {

  blogService: BlogService

  constructor(@Inject("BlogService") blogService: BlogService) {
    this.blogService = blogService
  }

  ngOnInit() {
  }

}
